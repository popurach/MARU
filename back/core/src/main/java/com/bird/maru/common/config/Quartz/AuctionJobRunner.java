package com.bird.maru.common.config.Quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AuctionJobRunner extends JobRunner{
    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetail = buildJobDetail(AuctionQuartzJob.class, "auctionJob", "batch", new HashMap());
//        Trigger trigger = buildJobTrigger("0 */2 * ? * *"); // 5분마다 실행
        Trigger trigger = buildJobTrigger("0 30 0 ? * MON"); // 매주 월요일 00:30마다 실행

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
