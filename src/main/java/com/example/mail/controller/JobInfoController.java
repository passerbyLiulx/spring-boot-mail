package com.example.mail.controller;

import com.example.mail.common.constants.ExceptionConstant;
import com.example.mail.model.JobInfoModel;
import com.example.mail.model.Result;
import com.example.mail.service.JobInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "定时任务管理")
@RestController
@RequestMapping("/job")
public class JobInfoController {

    @Autowired
    private JobInfoService jobInfoService;

    @ApiOperation("新增定时任务信息")
    @PostMapping("/addJobInfo")
    public Result addJob(@RequestBody JobInfoModel jobInfoModel) {
        String jobId = jobInfoService.saveJobInfo(jobInfoModel);
        return (StringUtils.isEmpty(jobId)) ? Result.error(ExceptionConstant.INSERT_FAILED_ERROR) : Result.ok(jobId);
    }

    @ApiOperation("更新定时任务信息")
    @PutMapping("/updateJobInfo")
    public Result updateJob(@RequestBody JobInfoModel jobInfoModel) {
        int updateCount = jobInfoService.updateJobInfo(jobInfoModel);
        return (updateCount == 0) ? Result.error(ExceptionConstant.UPDATE_FAILED_ERROR) : Result.ok(updateCount);
    }

    @ApiOperation("删除定时任务信息")
    @DeleteMapping("/deleteJobInfo/{jobId}")
    public Result delete(@PathVariable("jobId") String jobId) {
        int deleteCount = jobInfoService.deleteJobInfo(jobId);
        return (deleteCount == 0) ? Result.error(ExceptionConstant.SYSTEM_BUSY_ERROR) : Result.ok(deleteCount);
    }

    @ApiOperation("查询定时任务")
    @GetMapping("/getJobInfoById/{jobId}")
    public Result getJobById(@PathVariable("jobId") String jobId) {
        JobInfoModel jobInfoModel = jobInfoService.getJobInfoById(jobId);
        return Result.ok(jobInfoModel);
    }

    @ApiOperation("查询所有定时任务信息")
    @GetMapping("/jobInfoList")
    public Result getAllJob() {
        List<JobInfoModel> jobInfoModelList = jobInfoService.jobInfoList();
        return Result.ok(jobInfoModelList);
    }

    @ApiOperation("分页查询定时任务信息")
    @GetMapping("/jobInfoListByPage/{pageNum}/{pageSize}")
    public Result getAllJobPage(@PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize) {
        PageInfo<JobInfoModel> pageInfo = jobInfoService.jobInfoListByPage(pageNum, pageSize);
        return Result.ok(pageInfo);
    }

    @ApiOperation("条件分页查询定时任务信息")
    @PostMapping("/jobInfoListByPageCondition")
    public Result jobInfoListByPageCondition(@RequestBody(required = false) JobInfoModel jobInfoModel) {
        PageInfo<JobInfoModel> pageInfo = jobInfoService.jobInfoListByPageCondition(jobInfoModel);
        return Result.ok(pageInfo);
    }

    @ApiOperation("改变定时任务状态")
    @GetMapping("/changeTaskStatus/{jobId}/{openState}")
    public Result changeTaskStatus(@PathVariable("jobId") String jobId, @PathVariable("openState") int openState) {
        int update = jobInfoService.changeTaskStatus(jobId, openState);
        return (update == 0) ? Result.error(ExceptionConstant.UPDATE_FAILED_ERROR) : Result.ok(update);
    }

    @ApiOperation("ID标识是否重复")
    @GetMapping("/isRepeatById/{jobId}")
    public Result isRepeatById(@PathVariable("jobId") String jobId) {
        int infoCount = jobInfoService.countJobInfoById(jobId);
        return Result.ok(infoCount);
    }

}
