package com.bird.maru.common.config.Quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class MyJobListener implements JobListener {

    @Override
    public String getName() {
        return "MyJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("Job : " + context.getJobDetail().getKey().toString() + " is going to start...");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println("Job : " + context.getJobDetail().getKey().toString() + " execution is vetoed...");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("Job : " + context.getJobDetail().getKey().toString() + " is finished...");
    }

}
