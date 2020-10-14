package com.example.mail.model;

import lombok.Data;
import org.quartz.Job;
import org.quartz.JobKey;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class TaskModel {

    /**
     * 定时任务的名字和分组名
     */
    @NotNull(message = "定时任务的名字和组名坚决不为空")
    private JobKey jobKey;

    /**
     * 定时任务的描述
     */
    private String description;

    /**
     * 定时任务的执行cron表达式
     */
    private String cronExpression;

    /**
     * 定时任务的元数据
     */
    private Map<?, ?> jobDataMap;

    /**
     * 定时任务的具体执行逻辑类
     */
    @NotNull(message = "定时任务的具体执行逻辑类坚决不能为空")
    private Class<? extends Job> jobClass;

    public TaskModel(@NotNull(message = "定时任务的名字和组名坚决不为空") JobKey jobKey, String description,
                     String cronExpression, Map<?, ?> jobDataMap,
                     @NotNull(message = "定时任务的具体执行逻辑类坚决不能为空") Class<? extends Job> jobClass) {
        super();
        this.jobKey = jobKey;
        this.description = description;
        this.cronExpression = cronExpression;
        this.jobDataMap = jobDataMap;
        this.jobClass = jobClass;
    }
}
