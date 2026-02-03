package com.dressca.batch;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobOperatorTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.dressca.batch.job.BatchConfiguration;

/**
 * CatalogItem の Job の動作テストクラスです。
 */
@SpringBootTest
@SpringBatchTest
@SpringJUnitConfig(BatchConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CatalogItemJobTest {
  @Autowired
  private JobOperatorTestUtils jobOperatorTestUtils;

  @Autowired
  @Qualifier("catalogItem_job")
  Job catalogItemJob;
  @Autowired
  JobRepository jobRepository;
  private JdbcTemplate jdbcTemplate;
  private static final String OUTPUT_FILE = "output/outputData.csv";
  private static final String EXPECTED_FOLDER = "src/test/resources/expected/";

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   * ジョブオペレーターの初期化メソッドです。
   */
  @BeforeAll
  public void initJobOperatorTestUtils() {
    jobOperatorTestUtils.setJob(catalogItemJob);
  }

  /**
   * 各テストを実施する前のセットアップメソッドです。
   * 
   * @throws IOException 例外エラー。
   */
  @BeforeEach
  /* DB のテストデータと出力ファイルのクリーンアップ */
  public void clearData() throws IOException {
    jdbcTemplate.update("delete from catalog_items");
    jdbcTemplate.update("delete from catalog_item_assets");
    Files.deleteIfExists(Paths.get(OUTPUT_FILE));
  }

  /*
   * ジョブのエンドツーエンドのテスト：空データ
   */
  @Test
  public void jobTest_empty() throws Exception {
    JobExecution jobExecution = this.jobOperatorTestUtils.startJob();
    // 正常終了を確認
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    // 出力ファイルの確認
    String expectedFile = EXPECTED_FOLDER + "output_jobTest_empty.csv";
    String outputStr =
        (new FileSystemResource(OUTPUT_FILE)).getContentAsString(Charset.forName("UTF-8"));
    String expectedStr =
        (new FileSystemResource(expectedFile)).getContentAsString(Charset.forName("UTF-8"));
    // 期待値ファイルの改行コードは"\r\n"のため、出力ファイルの改行コード（OS依存）に変換して比較
    assertThat(outputStr)
        .isEqualTo(expectedStr.replaceAll("\r\n", System.getProperty("line.separator")));
  }

  /*
   * ジョブのエンドツーエンドのテスト：データ 10 件
   */
  @Test
  public void jobTest_10data() throws Exception {
    // テストデータ追加
    insertTestData();
    JobExecution jobExecution = this.jobOperatorTestUtils.startJob();
    // 正常終了を確認
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    // 出力ファイルの確認
    String expectedFile = EXPECTED_FOLDER + "output_jobTest_10data.csv";
    String outputStr =
        (new FileSystemResource(OUTPUT_FILE)).getContentAsString(Charset.forName("UTF-8"));
    String expectedStr =
        (new FileSystemResource(expectedFile)).getContentAsString(Charset.forName("UTF-8"));
    // 期待値ファイルの改行コードは"\r\n"のため、出力ファイルの改行コード（OS依存）に変換して比較
    assertThat(outputStr)
        .isEqualTo(expectedStr.replaceAll("\r\n", System.getProperty("line.separator")));
  }

  /*
   * ステップ単位でのテスト：データ 10 件
   */
  @Test
  public void stepTest_10data() throws Exception {
    // テストデータ追加
    insertTestData();
    // ステップを実行
    JobExecution jobExecution = this.jobOperatorTestUtils.startStep("catalogItem_step1");
    // 正常終了を確認
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    // 出力ファイルの確認
    String expectedFile = EXPECTED_FOLDER + "output_stepTest_10data.csv";
    String outputStr =
        (new FileSystemResource(OUTPUT_FILE)).getContentAsString(Charset.forName("UTF-8"));
    String expectedStr =
        (new FileSystemResource(expectedFile)).getContentAsString(Charset.forName("UTF-8"));
    // 期待値ファイルの改行コードは"\r\n"のため、出力ファイルの改行コード（ OS 依存）に変換して比較
    assertThat(outputStr)
        .isEqualTo(expectedStr.replaceAll("\r\n", System.getProperty("line.separator")));
  }

  private void insertTestData() {
    for (int i = 0; i < 10; i++) {
      String insertItem = "insert into catalog_items" + " (id,name,description,price,product_code,"
          + "catalog_category_id,catalog_brand_id,is_deleted,row_version)"
          + " values (?,?,?,1000,'C000000001'," + "1,1,false,'2024-01-01 00:00:00')";
      String insertItemAsset = "insert into catalog_item_assets (id,asset_code,catalog_item_id)"
          + " values (?,'dummy',?)";
      jdbcTemplate.update(insertItem, 101 + i, "sample" + i, "商品説明" + i);
      jdbcTemplate.update(insertItemAsset, 101 + i, 101 + i);
    }
  }
}
