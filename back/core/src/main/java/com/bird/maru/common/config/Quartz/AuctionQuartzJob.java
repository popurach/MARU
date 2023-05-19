package com.bird.maru.common.config.Quartz;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Slf4j
public class AuctionQuartzJob extends QuartzJobBean {
    private JobLauncher jobLauncher;
    private Job auctionLogsJob;
    private Job auctionAlarmJob;
    private Job auctionClosedJob;
    public AuctionQuartzJob(JobLauncher jobLauncher, Job auctionLogsJob, Job auctionAlarmJob, Job auctionClosedJob) {
        this.jobLauncher = jobLauncher;
        this.auctionLogsJob = auctionLogsJob;
        this.auctionAlarmJob = auctionAlarmJob;
        this.auctionClosedJob = auctionClosedJob;
    }

    /**
     * 배치를 실행시키는 구문 : 스케줄링된 이벤트가 발생할때마다 한번씩 호출된다.
     *
     * @param context : 예정
     * @throws org.springframework.batch.core.JobExecutionException : 예정
     */
    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        String jobName = context.getJobDetail().getJobDataMap().getString("jobName");

        if (jobName.equals("auctionLogsJob")) {
            // auctionLogsJob 실행
            jobLauncher.run(auctionLogsJob, jobParameters);
        } else if (jobName.equals("auctionAlarmJob")) {
            // auctionAlarmJob 실행
            jobLauncher.run(auctionAlarmJob, jobParameters);
        } else if (jobName.equals("auctionClosedJob")) {
            // auctionClosedJob 실행
            jobLauncher.run(auctionClosedJob, jobParameters);
        }
    }

}
