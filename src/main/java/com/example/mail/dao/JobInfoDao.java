package com.example.mail.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务
 */
@Data
public class JobInfoDao implements Serializable {

    /**
     * 任务ID
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务路径
     */
    private String taskPath;

    /**
     * 表达式
     */
    private String expression;

    /**
     * 表达式说明
     */
    private String expressionDescription;

    /**
     * 任务周期
     */
    private String taskCycle;

    /**
     * 执行节点
     */
    private String runNode;

    /**
     * 开启状态：0关闭 1开启
     */
    private Integer openState;

    /**
     * 删除状态 0：未删除 1：删除
     */
    private Integer deleteState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
