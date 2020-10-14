package com.example.mail.service.impl;

import com.example.mail.common.task.MyAdaptableJobFactory;
import com.example.mail.model.TaskModel;
import com.example.mail.service.ScheduleJobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private static Logger log = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);

    /***
     *Quartz定时任务核心的功能实现类
     **/
    private Scheduler scheduler;

    public ScheduleJobServiceImpl(@Autowired SchedulerFactoryBean schedulerFactoryBean,
            @Autowired MyAdaptableJobFactory myAdaptableJobFactory) {
        schedulerFactoryBean.setJobFactory(myAdaptableJobFactory);
        scheduler = schedulerFactoryBean.getScheduler();
    }


    /**
     * 创建定时任务
     * @param taskModel
     * @throws SchedulerException
     */
    public void scheduleJob(TaskModel taskModel) throws SchedulerException {
        // 1.定时任务的名字和组名
        JobKey jobKey = taskModel.getJobKey();
        // 2.定时任务的元数据
        JobDataMap jobDataMap = getJobDataMap(taskModel.getJobDataMap());
        // 3.定时任务的描述
        String description = taskModel.getDescription();
        // 4.定时任务的逻辑实现类
        Class<? extends Job> jobClass = taskModel.getJobClass();
        // 5.定时任务的cron表达式
        String cron = taskModel.getCronExpression();
        // 获取定时任务的信息
        JobDetail jobDetail = getJobDetail(jobKey, description, jobDataMap, jobClass);
        // 获取Trigger(Job的触发器,执行规则)
        Trigger trigger = getTrigger(jobKey, description, jobDataMap, cron);
        // 创建定时任务
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 暂停Job
     * @param jobKey
     * @throws SchedulerException
     */
    public void pauseJob(JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复Job
     * @param jobKey
     * @throws SchedulerException
     */
    public void resumeJob(JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除Job
     * @param jobKey
     * @throws SchedulerException
     */
    public void deleteJob(JobKey jobKey) throws SchedulerException {
        scheduler.deleteJob(jobKey);
    }

    /**
     * 定时任务的元数据
     * @param map
     * @return
     */
    public JobDataMap getJobDataMap(Map<?, ?> map) {
        return map == null ? new JobDataMap() : new JobDataMap(map);
    }

    /**
     * 获取定时任务的信息
     * @param jobKey
     * @param description
     * @param jobDataMap
     * @param jobClass
     * @return
     */
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap jobDataMap, Class<? extends Job> jobClass) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(jobDataMap)
                .usingJobData(jobDataMap)
                .requestRecovery()
                .storeDurably()
                .build();
    }

    /**
     * 获取Trigger(Job的触发器,执行规则)
     * @param jobKey
     * @param description
     * @param jobDataMap
     * @param cronExpression
     * @return
     */
    public Trigger getTrigger(JobKey jobKey, String description, JobDataMap jobDataMap, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .withDescription(description)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(jobDataMap)
                .build();
    }
}
