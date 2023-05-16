package com.bird.maru.common.config.Quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetail = buildJobDetail(AuctionQuartzJob.class, "auctionJob", "batch", new JobDataMap());
        JobDetail auctionAlarmJobDetail = buildJobDetail(AuctionQuartzJob.class, "auctionAlarmJob", "batch", new JobDataMap());
        JobDetail auctionClosedJobDetail = buildJobDetail(AuctionQuartzJob.class, "auctionClosedJob", "batch", new JobDataMap());

        jobDetail.getJobDataMap().put("jobName", "auctionLogsJob");
        auctionAlarmJobDetail.getJobDataMap().put("jobName", "auctionAlarmJob");
        auctionClosedJobDetail.getJobDataMap().put("jobName", "auctionClosedJob");

        Trigger trigger = buildJobTrigger("0 30 0 ? * *"); // 매일 00:30마다 실행
        Trigger auctionAlarmTrigger = buildJobTrigger("0 0 23 ? * *"); // 매일 23:00에 1시간 뒤 마감 알림
        Trigger auctionClosedTrigger = buildJobTrigger("0 0 0 ? * *"); // 매일 00:00 경매 종료 알림

//        Trigger trigger = buildJobTrigger("0 */1 * ? * *"); // 2분마다 실행
//        Trigger trigger = buildJobTrigger("0 30 0 ? * MON"); // 매주 월요일 00:30마다 실행
//        Trigger trigger = buildJobTrigger( "0 34 17 ? * *"); // 5분마다 실행
//        Trigger auctionAlarmTrigger = buildJobTrigger( "0 30 17 ? * *"); // 매일 23:00에 1시간 뒤 마감 알림
//        Trigger auctionClosedTrigger = buildJobTrigger( "0 32 17 ? * *"); // 매일 00:00 경매 종료 알림

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.scheduleJob(auctionAlarmJobDetail, auctionAlarmTrigger);
            scheduler.scheduleJob(auctionClosedJobDetail, auctionClosedTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
