package com.example.mail.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.mail.dao.JobInfoDao;
import com.example.mail.mapper.JobInfoMapper;
import com.example.mail.model.JobInfoModel;
import com.example.mail.model.TaskModel;
import com.example.mail.service.JobInfoService;
import com.example.mail.service.ScheduleJobService;
import com.example.mail.utils.task.QuartzUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    private static Logger logger = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 添加定时任务信息
     * @param jobInfoModel
     * @return
     */
    @Override
    public String saveJobInfo(JobInfoModel jobInfoModel) {
        JobInfoDao jobInfoDao = new JobInfoDao();
        BeanUtils.copyProperties(jobInfoModel, jobInfoDao);

        Map<String, Object> taskCycleMap = JSON.parseObject(jobInfoModel.getTaskCycle().toString(), Map.class);
        String expType = (String)taskCycleMap.get("type");
        if("1".equals(expType)){//间隔（秒）
            jobInfoDao.setExpression("0/"+(String)taskCycleMap.get("a")+" * * * * ?");
        }else if("2".equals(expType)){//天
            String sp [] = ((String)taskCycleMap.get("a")).split(":");
            jobInfoDao.setExpression(QuartzUtils.getDayQuartzExpression(
                    Integer.parseInt(sp[0]),
                    Integer.parseInt(sp[1]),
                    Integer.parseInt(sp[2]),
                    null));
        }else if("3".equals(expType)){//周
            String sp [] = ((String)taskCycleMap.get("b")).split(":");
            jobInfoDao.setExpression(Integer.parseInt(sp[2])+" "+Integer.parseInt(sp[1])+" "+Integer.parseInt(sp[0])+" ? * "+taskCycleMap.get("a"));
        }else if("4".equals(expType)){//月
            String sp [] = ((String)taskCycleMap.get("b")).split(":");
            jobInfoDao.setExpression(Integer.parseInt(sp[2])+" "+Integer.parseInt(sp[1])+" "+Integer.parseInt(sp[0])+" "+taskCycleMap.get("a")+" * ?");
        }else if("5".equals(expType)){//年
            String sp [] = ((String)taskCycleMap.get("c")).split(":");
            jobInfoDao.setExpression(Integer.parseInt(sp[2])+" "+Integer.parseInt(sp[1])+" "+Integer.parseInt(sp[0])+" "+taskCycleMap.get("b")+" "+taskCycleMap.get("a")+" ?");
        }else if("6".equals(expType)){//间隔（分）
            jobInfoDao.setExpression("0 0/"+(String)taskCycleMap.get("a")+" * * * ?");
        }else if("7".equals(expType)){//间隔（时）
            jobInfoDao.setExpression("0 0 0/"+(String)taskCycleMap.get("a")+" * * ?");
        }
        jobInfoDao.setOpenState(1);
        jobInfoDao.setDeleteState(0);
        int count = jobInfoMapper.addJobInfo(jobInfoDao);
        if (count > 0) {
            return jobInfoDao.getJobId();
        } else {
            return null;
        }
    }

    /**
     * 修改定时任务信息
     * @param jobInfoModel
     * @return
     */
    @Override
    public int updateJobInfo(JobInfoModel jobInfoModel) {
        JobInfoDao jobInfoDao = new JobInfoDao();
        BeanUtils.copyProperties(jobInfoModel, jobInfoDao);

        Map<String, Object> taskCycleMap = JSON.parseObject(jobInfoModel.getTaskCycle().toString(), Map.class);
        String expType = (String)taskCycleMap.get("type");
        if("1".equals(expType)){//间隔（秒）
            jobInfoDao.setExpression("0/"+(String)taskCycleMap.get("a")+" * * * * ?");
        }else if("2".equals(expType)){//天
            String sp [] = ((String)taskCycleMap.get("a")).split(":");
            jobInfoDao.setExpression(QuartzUtils.getDayQuartzExpression(
                    Integer.parseInt(sp[0]),
                    Integer.parseInt(sp[1]),
                    Integer.parseInt(sp[2]),
                    null));
        }else if("3".equals(expType)){//周
            String sp [] = ((String)taskCycleMap.get("b")).split(":");
            jobInfoDao.setExpression(Integer.parseInt(sp[2])+" "+Integer.parseInt(sp[1])+" "+Integer.parseInt(sp[0])+" ? * "+taskCycleMap.get("a"));
        }else if("4".equals(expType)){//月
            String sp [] = ((String)taskCycleMap.get("b")).split(":");
            jobInfoDao.setExpression(Integer.parseInt(sp[2])+" "+Integer.parseInt(sp[1])+" "+Integer.parseInt(sp[0])+" "+taskCycleMap.get("a")+" * ?");
        }else if("5".equals(expType)){//年
            String sp [] = ((String)taskCycleMap.get("c")).split(":");
            jobInfoDao.setExpression(Integer.parseInt(sp[2])+" "+Integer.parseInt(sp[1])+" "+Integer.parseInt(sp[0])+" "+taskCycleMap.get("b")+" "+taskCycleMap.get("a")+" ?");
        }else if("6".equals(expType)){//间隔（分）
            jobInfoDao.setExpression("0 0/"+(String)taskCycleMap.get("a")+" * * * ?");
        }else if("7".equals(expType)){//间隔（时）
            jobInfoDao.setExpression("0 0 0/"+(String)taskCycleMap.get("a")+" * * ?");
        }
        jobInfoDao.setOpenState(1);
        int count = jobInfoMapper.updateJobInfo(jobInfoDao);
        return count;
    }

    /**
     * 删除定时任务信息
     * @param jobId
     * @return
     */
    @Override
    public int deleteJobInfo(String jobId) {
        return jobInfoMapper.deleteJobInfo(jobId);
    }

    /**
     * 获取定时任务分页信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<JobInfoModel> jobInfoListByPage(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> jobInfoMapper.jobInfoList());
    }

    /**
     * 获取定时任务分页信息
     * @param jobInfoModel
     * @return
     */
    @Override
    public PageInfo<JobInfoModel> jobInfoListByPageCondition(JobInfoModel jobInfoModel) {
        JobInfoDao jobInfoDao = new JobInfoDao();
        BeanUtils.copyProperties(jobInfoModel, jobInfoDao);
        PageInfo<JobInfoModel> jobInfoPage = PageHelper.startPage(jobInfoModel.getPageNum(), jobInfoModel.getPageSize()).doSelectPageInfo(() -> {
            jobInfoMapper.jobInfoListByPageCondition(jobInfoModel);
        });
        return jobInfoPage;
    }

    /**
     * 获取定时任务集合信息
     * @return
     */
    @Override
    public List<JobInfoModel> jobInfoList() {
        List<JobInfoDao> jobInfoDaoList = jobInfoMapper.jobInfoListByOpen();
        List<JobInfoModel> jobInfoModelList = new ArrayList<>();
        for (JobInfoDao jobInfoDao : jobInfoDaoList) {
            JobInfoModel jobInfoModel = new JobInfoModel();
            BeanUtils.copyProperties(jobInfoDao, jobInfoModel);
            jobInfoModelList.add(jobInfoModel);
        }
        return jobInfoModelList;
    }

    /**
     * 根据Id获取定时任务信息
     * @param jobId
     * @return
     */
    @Override
    public JobInfoModel getJobInfoById(String jobId) {
        JobInfoModel jobInfoModel = new JobInfoModel();
        JobInfoDao jobInfoDao = jobInfoMapper.getJobInfoById(jobId);
        BeanUtils.copyProperties(jobInfoDao, jobInfoModel);
        return jobInfoModel;
    }

    /**
     * 根据Id获取定时任务数量
     * @param jobId
     * @return
     */
    @Override
    public int countJobInfoById(String jobId) {
        return jobInfoMapper.countJobInfoById(jobId);
    }

    /**
     * 修改定时任务状态
     * @param jobId
     * @param openState
     * @return
     */
    @Override
    public int changeTaskStatus(String jobId, int openState) {
        JobInfoDao jobInfoDao = jobInfoMapper.getJobInfoById(jobId);
        jobInfoDao.setOpenState(openState);
        int update = jobInfoMapper.updateJobInfo(jobInfoDao);
        JobInfoModel jobInfoModel = new JobInfoModel();
        BeanUtils.copyProperties(jobInfoDao, jobInfoModel);
        try {
            closeJobTask(jobInfoModel);
            if (openState == 1) {
                openJobTask(jobInfoModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    /**
     * 获取开启的定时任务集合
     * @return
     */
    @Override
    public List<JobInfoModel> jobInfoListByOpen() {
        List<JobInfoDao> jobInfoDaoList = jobInfoMapper.jobInfoList();
        List<JobInfoModel> jobInfoModelList = new ArrayList<>();
        for (JobInfoDao jobInfoDao : jobInfoDaoList) {
            JobInfoModel jobInfoModel = new JobInfoModel();
            BeanUtils.copyProperties(jobInfoDao, jobInfoModel);
            jobInfoModelList.add(jobInfoModel);
        }
        return jobInfoModelList;
    }

    /**
     * 开启定时任务
     * @param jobInfoModel
     * @throws Exception
     */
    @Override
    public void openJobTask(JobInfoModel jobInfoModel) throws Exception {
        HashMap<String, Object> jobMap = new HashMap<>();
        jobMap.put("job", jobInfoModel);
        String taskPath = jobInfoModel.getTaskPath();
        Class<? extends Job> jobClass;
        TaskModel define = null;
        jobClass  = (Class<? extends Job>) Class.forName(taskPath).newInstance().getClass();
        define = new TaskModel(new JobKey("jd" + jobInfoModel.getJobId(), "jg" + jobInfoModel.getJobId()),
                jobInfoModel.getExpressionDescription(), jobInfoModel.getExpression(), jobMap, jobClass);
        scheduleJobService.scheduleJob(define);
    }

    /**
     * 关闭定时任务
     * @param jobInfoModel
     * @throws SchedulerException
     */
    @Override
    public void closeJobTask(JobInfoModel jobInfoModel) throws SchedulerException {
        scheduleJobService.pauseJob(new JobKey("jd" + jobInfoModel.getJobId(), "jg" + jobInfoModel.getJobId()));
        scheduleJobService.deleteJob(new JobKey("jd" + jobInfoModel.getJobId(), "jg" + jobInfoModel.getJobId()));
    }
}

