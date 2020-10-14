package com.example.mail.service;

import com.example.mail.model.TaskModel;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

public interface ScheduleJobService {

    /**
     * 创建定时任务
     * @param taskModel
     */
    public void scheduleJob(TaskModel taskModel) throws SchedulerException;

    /**
     * 暂停Job
     * @param jobKey
     */
    public void pauseJob(JobKey jobKey) throws SchedulerException;

    /**
     * 恢复Job
     * @param jobKey
     */
    public void resumeJob(JobKey jobKey) throws SchedulerException;

    /**
     * 删除Job
     * @param jobKey
     */
    public void deleteJob(JobKey jobKey) throws SchedulerException;
}
