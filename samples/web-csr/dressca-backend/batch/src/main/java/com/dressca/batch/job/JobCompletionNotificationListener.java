package com.dressca.batch.job;

import com.dressca.domainmodules.common.log.AbstractStructuredLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * ジョブの完了を通知するためのクラスです。
 */
@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

  private final AbstractStructuredLogger apLog;

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      apLog.info("!!! JOB FINISHED!");
    }
  }
}
