package com.dressca.batch.job;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dressca.systemcommon.log.AbstractStructuredLogger;

/**
 * ジョブの完了を通知するためのクラスです。
 */
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

  @Autowired
  private AbstractStructuredLogger apLog;

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      apLog.info("!!! JOB FINISHED!");
    }
  }
}
