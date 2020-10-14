package com.example.mail.service;


import com.example.mail.model.JobInfoModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface JobInfoService {

    /**
     * 添加定时任务信息
     * @param jobInfomodel
     * @return
     */
    String saveJobInfo(JobInfoModel jobInfomodel);

    /**
     * 修改定时任务信息
     * @param jobInfomodel
     * @return
     */
    int updateJobInfo(JobInfoModel jobInfomodel);

    /**
     * 删除定时任务信息
     * @param jobId
     * @return
     */
    int deleteJobInfo(String jobId);

    /**
     * 获取定时任务分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<JobInfoModel> jobInfoListByPage(int pageNum, int pageSize);

    /**
     * 获取定时任务分页信息
     * @param jobInfoModel
     * @return
     */
    PageInfo<JobInfoModel> jobInfoListByPageCondition(JobInfoModel jobInfoModel);

    /**
     * 根据Id获取定时任务信息
     * @param jobId
     * @return
     */
    JobInfoModel getJobInfoById(String jobId);

    /**
     * 获取定时任务集合信息
     * @return
     */
    List<JobInfoModel> jobInfoList();

    /**
     * 修改定时任务状态
     * @param jobId
     * @param isOpen
     * @return
     */
    int changeTaskStatus(String jobId, int isOpen);

    /**
     * 根据Id获取定时任务数量
     * @param jobId
     * @return
     */
    int countJobInfoById(String jobId);

    /**
     * 获取开启的定时任务集合
     * @return
     */
    List<JobInfoModel> jobInfoListByOpen();

    /**
     * 开启定时任务
     * @param jobInfoModel
     * @throws Exception
     */
    void openJobTask(JobInfoModel jobInfoModel) throws Exception;

    /**
     * 关闭定时任务
     * @param jobInfoModel
     * @throws Exception
     */
    void closeJobTask(JobInfoModel jobInfoModel) throws Exception;

}