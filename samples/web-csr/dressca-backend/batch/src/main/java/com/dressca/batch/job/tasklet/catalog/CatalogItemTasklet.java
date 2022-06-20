package com.dressca.batch.job.tasklet.catalog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;

/**
 * catalog_item_tasklet_jobで実行されるTaskletクラス。
 */
@Component
@StepScope
public class CatalogItemTasklet implements Tasklet {

  @Autowired
  private CatalogRepository repository;
  @Value("#{jobParameters['output']}")
  String output;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    // DBからCatalogItemを全件取得
    List<CatalogItem> catalogItemList = repository.findWithPaging(0, 1000);
    List<CatalogItem> convertedList = new ArrayList<>();
    // 商品名を先頭10文字にする
    catalogItemList.forEach(it -> {
      CatalogItem item = new CatalogItem();
      BeanUtils.copyProperties(it, item);
      String name = it.getName();
      if (name.length() > 10) {
        item.setName(name.substring(0, 10));
      }
      convertedList.add(item);
    });

    // CSVへ出力するwriterの準備
    FlatFileItemWriter<CatalogItem> writer = new FlatFileItemWriter<>();
    Resource outputResource;
    if (output == null || "".equals(output)) {
      outputResource = new FileSystemResource("output/catalogItem_tasklet.csv");
    } else {
      outputResource = new FileSystemResource("output/" + output);
    }
    writer.setResource(outputResource);
    writer.setAppendAllowed(true);
    writer.setLineAggregator(new DelimitedLineAggregator<CatalogItem>() {
      {
        setDelimiter(",");
        setFieldExtractor(new BeanWrapperFieldExtractor<CatalogItem>() {
          {
            setNames(new String[] { "name", "price", "productCode" });
          }
        });
      }
    });
    // CSV出力
    writer.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
    writer.write(convertedList);
    writer.close();
    return RepeatStatus.FINISHED;
  }
}
