package com.example.mail.controller;

import com.example.mail.common.constants.ExceptionConstant;
import com.example.mail.common.enums.ExceptionEnum;
import com.example.mail.model.JobInfoModel;
import com.example.mail.model.ResultModel;
import com.example.mail.model.ResultModelTest;
import com.example.mail.service.JobInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
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
    public ResultModel addJob(@RequestBody JobInfoModel jobInfoModel) {
        String jobId = jobInfoService.saveJobInfo(jobInfoModel);
        return (StringUtils.isEmpty(jobId)) ? ResultModel.error(ExceptionConstant.INSERT_FAILED_ERROR) : ResultModel.ok(jobId);
    }

    @ApiOperation("更新定时任务信息")
    @PutMapping("/updateJobInfo")
    public ResultModel updateJob(@RequestBody JobInfoModel jobInfoModel) {
        int updateCount = jobInfoService.updateJobInfo(jobInfoModel);
        return (updateCount == 0) ? ResultModel.error(ExceptionConstant.UPDATE_FAILED_ERROR) : ResultModel.ok(updateCount);
    }

    @ApiOperation("删除定时任务信息")
    @DeleteMapping("/deleteJobInfo/{jobId}")
    public ResultModel delete(@PathVariable("jobId") String jobId) {
        int deleteCount = jobInfoService.deleteJobInfo(jobId);
        return (deleteCount == 0) ? ResultModel.error(ExceptionConstant.SYSTEM_BUSY_ERROR) : ResultModel.ok(deleteCount);
    }

    @ApiOperation("查询定时任务")
    @GetMapping("/getJobInfoById/{jobId}")
    public ResultModel getJobById(@PathVariable("jobId") String jobId) {
        JobInfoModel jobInfoModel = jobInfoService.getJobInfoById(jobId);
        return ResultModel.ok(jobInfoModel);
    }

    @ApiOperation("查询所有定时任务信息")
    @GetMapping("/jobInfoList")
    public ResultModel getAllJob() {
        List<JobInfoModel> jobInfoModelList = jobInfoService.jobInfoList();
        return ResultModel.ok(jobInfoModelList);
    }

    @ApiOperation("分页查询定时任务信息")
    @GetMapping("/jobInfoListByPage/{pageNum}/{pageSize}")
    public ResultModel getAllJobPage(@PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize) {
        PageInfo<JobInfoModel> pageInfo = jobInfoService.jobInfoListByPage(pageNum, pageSize);
        return ResultModel.ok(pageInfo);
    }

    @ApiOperation("条件分页查询定时任务信息")
    @PostMapping("/jobInfoListByPageCondition")
    public ResultModel jobInfoListByPageCondition(@RequestBody(required = false) JobInfoModel jobInfoModel) {
        PageInfo<JobInfoModel> pageInfo = jobInfoService.jobInfoListByPageCondition(jobInfoModel);
        return ResultModel.ok(pageInfo);
    }

    @ApiOperation("改变定时任务状态")
    @GetMapping("/changeTaskStatus/{jobId}/{openState}")
    public ResultModel changeTaskStatus(@PathVariable("jobId") String jobId, @PathVariable("openState") int openState) {
        int update = jobInfoService.changeTaskStatus(jobId, openState);
        return (update == 0) ? ResultModel.error(ExceptionConstant.UPDATE_FAILED_ERROR) : ResultModel.ok(update);
    }

    @ApiOperation("ID标识是否重复")
    @GetMapping("/isRepeatById/{jobId}")
    public ResultModel isRepeatById(@PathVariable("jobId") String jobId) {
        int infoCount = jobInfoService.countJobInfoById(jobId);
        return ResultModel.ok(infoCount);
    }

    @ApiOperation("测试demo1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramOne", value = "参数1", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "paramTwo", value = "参数2", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponse(code = 200, message = "成功", response = ResultModelTest.class)
    @GetMapping("/testOne")
    public ResultModelTest<JobInfoModel> testOne(@RequestParam("paramOne") String paramOne, @RequestParam("paramTwo") String paramTwo) {
        JobInfoModel jobInfoModel = new JobInfoModel();
        jobInfoModel.setJobId("111");
        jobInfoModel.setTaskName("testOne");
        return new ResultModelTest<>(200, "成功", jobInfoModel);
    }

    @ApiOperation("测试demo2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramOne", value = "参数1", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "paramTwo", value = "参数2", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponse(code = 200, message = "成功", response = ResultModelTest.class)
    @GetMapping("/testTwo")
    public ResultModelTest<JobInfoModel> testTwo(@RequestParam("paramOne") String paramOne, @RequestParam("paramTwo") String paramTwo) {
        JobInfoModel jobInfoModel = new JobInfoModel();
        jobInfoModel.setJobId("111");
        jobInfoModel.setTaskName("testOne");
        return new ResultModelTest<>(200, "成功", jobInfoModel);
    }
}
