package com.example.mail.job;

import com.alibaba.fastjson.JSON;
import com.example.mail.common.constants.MailSendStateConstant;
import com.example.mail.common.constants.MailTypeConstant;
import com.example.mail.dao.MailInfoDao;
import com.example.mail.mapper.MailInfoMapper;
import com.example.mail.model.MailInfoModel;
import com.example.mail.service.MailInfoService;
import io.netty.util.internal.StringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MailInfoJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(MailInfoJob.class);


    @Autowired
    private MailInfoMapper mailInfoMapper;

    @Autowired
    private MailInfoService mailInfoService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<MailInfoDao> mailInfoDaoList = mailInfoMapper.mailInfoListBySendState(MailSendStateConstant.MAIL_SEND_FAIL);
        for (MailInfoDao mailInfoDao : mailInfoDaoList) {
            MailInfoModel mailInfoModel = new MailInfoModel();
            BeanUtils.copyProperties(mailInfoDao, mailInfoModel);
            mailInfoModel.setToArr(mailInfoDao.getMailTo().split(","));
            if (!StringUtil.isNullOrEmpty(mailInfoDao.getMailCc())) {
                mailInfoModel.setCcArr(mailInfoDao.getMailCc().split(","));
            }
            if (!StringUtil.isNullOrEmpty(mailInfoDao.getMailBcc())) {
                mailInfoModel.setBccArr(mailInfoDao.getMailBcc().split(","));
            }
            try {
                // 使用 switch case
                /*switch (mailInfoDao.getMailType()) {
                    case MailTypeConstant.MAIL_TYPE_SIMPLE:
                        mailInfoService.sendSimpleMail(mailInfoModel);
                        break;
                    case MailTypeConstant.MAIL_TYPE_INLINE:
                        if (!StringUtil.isNullOrEmpty(mailInfoDao.getAttachmentPath())) {
                            Map<String, Object> pictureMap = JSON.parseObject(mailInfoDao.getAttachmentPath(), Map.class);
                            mailInfoModel.setPictureMap(pictureMap);
                        }
                        mailInfoService.sendSimpleMail(mailInfoModel);
                        break;
                    case MailTypeConstant.MAIL_TYPE_ATTACHMENT:
                        if (!StringUtil.isNullOrEmpty(mailInfoDao.getAttachmentPath())) {
                            mailInfoModel.setAttachmentPathArr(mailInfoDao.getAttachmentPath().split("\\|"));
                        }
                        mailInfoService.sendAttachmentMail(mailInfoModel);
                        break;
                    case MailTypeConstant.MAIL_TYPE_TEMPLATE:
                        if (!StringUtil.isNullOrEmpty(mailInfoDao.getTemplateJson())) {
                            Map<String, Object> templateMap = JSON.parseObject(mailInfoDao.getTemplateJson().toString(), Map.class);
                            mailInfoModel.setTemplateMap(templateMap);
                        }
                        mailInfoService.sendTemplateMail(mailInfoModel);
                        break;
                }*/
                // 使用 if else
                /*if (MailTypeConstant.MAIL_TYPE_SIMPLE == mailInfoDao.getMailType()) {
                    mailInfoService.sendSimpleMail(mailInfoModel);
                } else if (MailTypeConstant.MAIL_TYPE_INLINE == mailInfoDao.getMailType()) {
                    if (!StringUtil.isNullOrEmpty(mailInfoDao.getAttachmentPath())) {
                        Map<String, Object> pictureMap = JSON.parseObject(mailInfoDao.getAttachmentPath(), Map.class);
                        mailInfoModel.setPictureMap(pictureMap);
                    }
                    mailInfoService.sendSimpleMail(mailInfoModel);
                } else if (MailTypeConstant.MAIL_TYPE_ATTACHMENT == mailInfoDao.getMailType()) {
                    if (!StringUtil.isNullOrEmpty(mailInfoDao.getAttachmentPath())) {
                        mailInfoModel.setAttachmentPathArr(mailInfoDao.getAttachmentPath().split("\\|"));
                    }
                    mailInfoService.sendAttachmentMail(mailInfoModel);
                } else if (MailTypeConstant.MAIL_TYPE_TEMPLATE == mailInfoDao.getMailType()) {
                    if (!StringUtil.isNullOrEmpty(mailInfoDao.getTemplateJson())) {
                        Map<String, Object> templateMap = JSON.parseObject(mailInfoDao.getTemplateJson().toString(), Map.class);
                        mailInfoModel.setTemplateMap(templateMap);
                    }
                    mailInfoService.sendTemplateMail(mailInfoModel);
                }*/
            } catch (Exception e) {
                logger.error("发送邮件失败", e);
                e.printStackTrace();
            }
        }
    }
}