package com.example.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.mail.common.constants.MailDeleteStateConstant;
import com.example.mail.common.constants.MailSendStateConstant;
import com.example.mail.common.constants.MailTypeConstant;
import com.example.mail.common.enums.ExceptionEnum;
import com.example.mail.dao.MailInfoDao;
import com.example.mail.mapper.MailInfoMapper;
import com.example.mail.model.MailInfoModel;
import com.example.mail.model.ResultModel;
import com.example.mail.service.MailInfoService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class MailInfoServiceImpl implements MailInfoService {

    private final Logger logger = LoggerFactory.getLogger(MailInfoServiceImpl.class);

    @Autowired
    private MailInfoMapper mailInfoMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    public String from;

    /**
     * 简单文本邮件
     *
     * @param mailModel
     */
    @Override
    public ResultModel sendSimpleMail(MailInfoModel mailModel) throws Exception {
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel);
        if ((int)resultModel.get("code") != ExceptionEnum.SUCCESS.getCode()) {
            return resultModel;
        }
        try {
            // 简单邮件消息对象
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            // setTo 接收人，setCc 抄送人，setBCc密送人
            simpleMailMessage.setTo(mailModel.getToArr());
            if (mailModel.getCcArr() != null) {
                simpleMailMessage.setCc(mailModel.getCcArr());
            }
            if (mailModel.getBccArr() != null) {
                simpleMailMessage.setBcc(mailModel.getBccArr());
            }
            simpleMailMessage.setSubject(mailModel.getSubject());
            simpleMailMessage.setText(mailModel.getContent());
            simpleMailMessage.setSentDate(new Date());
            mailSender.send(simpleMailMessage);
            logger.info("发送邮件成功");
            mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                // 保存邮件信息
                saveMailInfo(mailModel,  MailTypeConstant.MAIL_TYPE_SIMPLE);
            } else {
                // 修改邮件信息
                updateMailInfo(mailModel);
            }
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 3) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendSimpleMail(mailModel);
            }
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_SIMPLE);
            }
            logger.error("发送邮件失败", e);
            return ResultModel.error();
        }
        return ResultModel.ok();
    }

    /**
     * 带图片邮件
     *
     * @param mailModel
     */
    @Override
    public ResultModel sendInlineMail(MailInfoModel mailModel) throws Exception {
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel);
        if ((int)resultModel.get("code") != ExceptionEnum.SUCCESS.getCode()) {
            return resultModel;
        }
        // MIME协议
        MimeMessage mimeMessage;
        try {
            mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setFrom(from);
            // setTo 接收人，setCc 抄送人，setBCc密送人
            mimeMessageHelper.setTo(mailModel.getToArr());
            if (mailModel.getCcArr() != null) {
                mimeMessageHelper.setCc(mailModel.getCcArr());
            }
            if (mailModel.getBccArr() != null) {
                mimeMessageHelper.setBcc(mailModel.getBccArr());
            }
            mimeMessageHelper.setSubject(mailModel.getSubject());
            mimeMessageHelper.setText(mailModel.getContent(), true);
            if (mailModel.getPictureMap() != null) {
                // 添加多个图片
                for (Map.Entry<String, Object> entry : mailModel.getPictureMap().entrySet()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(new File((String) entry.getValue()));
                    mimeMessageHelper.addInline(entry.getKey(), fileSystemResource);
                }
            }
            mimeMessageHelper.setSentDate(new Date());
            mailSender.send(mimeMessage);
            logger.info("发送邮件成功");
            mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_INLINE);
            } else {
                // 修改邮件信息
                updateMailInfo(mailModel);
            }
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 3) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendInlineMail(mailModel);
            }
             if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                 mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                 // 保存邮件信息
                 saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_INLINE);
            }
            logger.error("发送邮件失败", e);
            return ResultModel.error();
        }
        return ResultModel.ok();

    }

    /**
     * 带附件邮件
     *
     * @param mailModel
     */
    @Override
    public ResultModel sendAttachmentMail(MailInfoModel mailModel) throws Exception {
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel);
        if ((int)resultModel.get("code") != ExceptionEnum.SUCCESS.getCode()) {
            return resultModel;
        }
        // MIME协议
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setFrom(from);
            // setTo 接收人，setCc 抄送人，setBCc密送人
            mimeMessageHelper.setTo(mailModel.getToArr());
            if (mailModel.getCcArr() != null) {
                mimeMessageHelper.setCc(mailModel.getCcArr());
            }
            if (mailModel.getBccArr() != null) {
                mimeMessageHelper.setBcc(mailModel.getBccArr());
            }
            mimeMessageHelper.setSubject(mailModel.getSubject());
            mimeMessageHelper.setText(mailModel.getContent());
            if (mailModel.getAttachmentPathArr() != null) {
                // 添加多个文件
                /*Arrays.stream(mailModel.getAttachmentPathArr()).forEach(attachmentPath -> {
                    String fileName = attachmentPath.substring(attachmentPath.lastIndexOf(File.separator));
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachmentPath));
                    mimeMessageHelper.addAttachment(fileName, fileSystemResource);
                });*/
                // 添加多个文件
                for (String attachmentPath : mailModel.getAttachmentPathArr()) {
                    String fileName = attachmentPath.substring(attachmentPath.lastIndexOf(File.separator) + 1);
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachmentPath));
                    mimeMessageHelper.addAttachment(fileName, fileSystemResource);
                }
            }
            /*for (MultipartFile multipartFile : mailModel.getMultipartFiles()) {
                mimeMessageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
            }*/
            mimeMessageHelper.setSentDate(new Date());
            mailSender.send(mimeMessage);
            logger.info("发送邮件成功");
            mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_ATTACHMENT);
            } else {
                // 修改邮件信息
                updateMailInfo(mailModel);
            }
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 3) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendAttachmentMail(mailModel);
            }
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_ATTACHMENT);
            }
            logger.error("发送邮件失败", e);
            return ResultModel.error();
        }
        return ResultModel.ok();
    }

    /**
     * 带模板邮件
     *
     * @param mailModel
     */
    @Override
    public JSONObject sendTemplateMail(MailInfoModel mailModel) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel);
        /*if ((int)resultModel.get("code") != ExceptionEnum.SUCCESS.getCode()) {
            return resultModel;
        }*/
        /*if (!resultModel.containsValue(ExceptionEnum.SUCCESS.getCode())) {
            return resultModel;
        }*/
        // MIME协议
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setFrom(from);
            // setTo 接收人，setCc 抄送人，setBCc密送人
            if (mailModel.getToArr() != null) {
                mimeMessageHelper.setTo(mailModel.getToArr());
            }
            if (mailModel.getCcArr() != null) {
                mimeMessageHelper.setCc(mailModel.getCcArr());
            }
            if (mailModel.getBccArr() != null) {
                mimeMessageHelper.setBcc(mailModel.getBccArr());
            }
            mimeMessageHelper.setSubject(mailModel.getSubject());
            if (StringUtil.isNullOrEmpty(mailModel.getTemplateName())) {
                throw new Exception("模板名称不能为空");
            }
            // 添加正文（使用thymeleaf模板）
            Context context = new Context();
            if (mailModel.getTemplateMap() != null) {
                context.setVariables(mailModel.getTemplateMap());
            }
            String content = templateEngine.process(mailModel.getTemplateName(), context);
            mimeMessageHelper.setText(content, true);
            if (mailModel.getAttachmentPathArr() != null) {
                // 添加多个文件
                for (String attachmentPath : mailModel.getAttachmentPathArr()) {
                    String fileName = attachmentPath.substring(attachmentPath.lastIndexOf(File.separator) + 1);
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachmentPath));
                    mimeMessageHelper.addAttachment(fileName, fileSystemResource);
                }
            }
            mimeMessageHelper.setSentDate(new Date());
            mailSender.send(mimeMessage);
            logger.info("发送邮件成功");
            mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_TEMPLATE);
            } else {
                // 修改邮件信息
                updateMailInfo(mailModel);
            }
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 3) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendTemplateMail(mailModel);
            }
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_TEMPLATE);
            }
            logger.error("发送邮件失败", e);
            jsonObject.put("code", 500);
            jsonObject.put("msg", "系统异常");
            jsonObject.put("data", "");
            return jsonObject;
        }
        jsonObject.put("code", 200);
        jsonObject.put("msg", "成功");
        jsonObject.put("data", "");
        return jsonObject;
    }

    /**
     * 校验基础邮件信息
     *
     * @param mailModel
     * @throws Exception
     */
    public ResultModel checkBaseMailInfo(MailInfoModel mailModel) throws Exception {
        if (StringUtil.isNullOrEmpty(from)) {
            return ResultModel.error(ExceptionEnum.EMAIL_FROM_NOT_NULL_EXCEPTION.getMessage());
        }
        if (mailModel.getToArr() == null) {
            return ResultModel.error(ExceptionEnum.EMAIL_TO_NOT_NULL_EXCEPTION.getMessage());
        }
        if (StringUtil.isNullOrEmpty(mailModel.getSubject())) {
            return ResultModel.error(ExceptionEnum.EMAIL_SUBJECT_NOT_NULL_EXCEPTION.getMessage());
        }
        if (StringUtil.isNullOrEmpty(mailModel.getContent())) {
            return ResultModel.error(ExceptionEnum.EMAIL_CONTENT_NOT_NULL_EXCEPTION.getMessage());
        }
        return ResultModel.ok();
    }

    /**
     * 保存邮件信息
     *
     * @param mailModel
     */
    public void saveMailInfo(MailInfoModel mailModel, Integer mailType) throws Exception {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MailInfoDao mailDao = new MailInfoDao();
        mailDao.setMailId(uuid);
        mailDao.setMailFrom(from);
        mailDao.setMailTo(StringUtils.join(mailModel.getToArr(), ","));
        if (mailModel.getCcArr() != null) {
            mailDao.setMailCc(StringUtils.join(mailModel.getCcArr(), ","));
        }
        if (mailModel.getBccArr() != null) {
            mailDao.setMailBcc(StringUtils.join(mailModel.getBccArr(), ","));
        }
        mailDao.setSubject(mailModel.getSubject());
        mailDao.setContent(mailModel.getContent());
        if (mailModel.getPictureMap() != null) {
            String pictureJson = JSONObject.toJSONString(mailModel.getPictureMap());
            mailDao.setPictureJson(pictureJson);
        }
        if (mailModel.getAttachmentPathArr() != null) {
            mailDao.setAttachmentPath(StringUtils.join(mailModel.getAttachmentPathArr(), "|"));
        }
        mailDao.setTemplateName(mailModel.getTemplateName());
        if (mailModel.getTemplateMap() != null) {
            String templateJson = JSONObject.toJSONString(mailModel.getTemplateMap());
            mailDao.setTemplateJson(templateJson);
        }
        mailDao.setSendDate(new Date());
        mailDao.setMailType(mailType);
        mailDao.setSendState(mailModel.getSendState());
        mailDao.setDeleteState(MailDeleteStateConstant.MAIL_INFO_NOT_DELETED);
        mailInfoMapper.addMailInfo(mailDao);

        /*int count = mailInfoMapper.addMailInfo(mailDao);
        if (count != 1) {
            throw new Exception("保存邮件信息失败");
        }
        logger.info("保存邮件信息成功");*/
    }

    /**
     * 修改邮件状态
     *
     * @param mailModel
     * @throws Exception
     */
    public void updateMailInfo(MailInfoModel mailModel) throws Exception {
        MailInfoDao mailInfoDao = mailInfoMapper.getMailInfoByMailId(mailModel.getMailId());
        mailInfoDao.setSendState(mailModel.getSendState());
        mailInfoDao.setSendDate(new Date());
        mailInfoMapper.updateMailInfo(mailInfoDao);

        /*int count = mailInfoMapper.updateMailInfo(mailInfoDao);
        if (count != 1) {
            throw new Exception("修改邮件信息失败");
        }
        logger.info("修改邮件信息成功");*/
    }
}