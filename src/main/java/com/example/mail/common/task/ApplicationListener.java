package com.example.mail.common.task;

import com.example.mail.model.JobInfoModel;
import com.example.mail.service.JobInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationListener implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public void run(String... args) throws Exception {

        // 应用启动之后执行所有可执行的的任务
        List<JobInfoModel> jobInfoDaoList = jobInfoService.jobInfoListByOpen();
        System.out.println(jobInfoDaoList);
        for (JobInfoModel jobInfoModel : jobInfoDaoList) {
            jobInfoService.openJobTask(jobInfoModel);
        }
    }
}
