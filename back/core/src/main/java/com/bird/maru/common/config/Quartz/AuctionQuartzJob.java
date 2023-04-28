package com.bird.maru.common.config.Quartz;

import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuctionQuartzJob extends QuartzJobBean {
    @Autowired
    private Job auctionLogsJob;

    @Autowired
    private JobLauncher jobLauncher;

    /**
     * 배치를 실행시키는 구문 : 스케줄링된 이벤트가 발생할때마다 한번씩 호출된다.
     * @param context : 예정
     * @throws org.springframework.batch.core.JobExecutionException : 예정
     * */
    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        jobLauncher.run(auctionLogsJob, jobParameters);
    }
}
