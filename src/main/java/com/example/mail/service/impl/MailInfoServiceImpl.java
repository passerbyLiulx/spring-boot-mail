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
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
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
        ResultModel resultModel = checkBaseMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_SIMPLE);
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
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 2) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendSimpleMail(mailModel);
            }
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_SIMPLE);
            }
            logger.error("发送邮件失败", e);
            return ResultModel.error(ExceptionEnum.EMAIL_SEND_FAIL.getMessage());
        }
        mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
        if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
            // 保存邮件信息
            return saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_SIMPLE);
        } else {
            // 修改邮件信息
            return updateMailInfo(mailModel);
        }
    }

    /**
     * 带图片邮件
     *
     * @param mailModel
     */
    @Override
    public ResultModel sendInlineMail(MailInfoModel mailModel) throws Exception {
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_INLINE);
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
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 2) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendInlineMail(mailModel);
            }
             if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                 mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                 // 保存邮件信息
                 saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_INLINE);
            }
            logger.error("发送邮件失败", e);
            return ResultModel.error(ExceptionEnum.EMAIL_SEND_FAIL.getMessage());
        }
        mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
        if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
            // 保存邮件信息
            return saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_INLINE);
        } else {
            // 修改邮件信息
            return updateMailInfo(mailModel);
        }

    }

    /**
     * 带附件邮件
     *
     * @param mailModel
     */
    @Override
    public ResultModel sendAttachmentMail(MailInfoModel mailModel) throws Exception {
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_ATTACHMENT);
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
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 2) {
                mailModel.setRetryNumber(mailModel.getRetryNumber() + 1);
                return sendAttachmentMail(mailModel);
            }
            if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
                mailModel.setSendState(MailSendStateConstant.MAIL_SEND_FAIL);
                // 保存邮件信息
                saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_ATTACHMENT);
            }
            logger.error("发送邮件失败", e);
            return ResultModel.error(ExceptionEnum.EMAIL_SEND_FAIL.getMessage());
        }
        mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
        if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
            // 保存邮件信息
            return saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_ATTACHMENT);
        } else {
            // 修改邮件信息
            return updateMailInfo(mailModel);
        }
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
        ResultModel resultModel = checkBaseMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_TEMPLATE);
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
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 2) {
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
            jsonObject.put("msg", "系统异常，请稍后重试");
            jsonObject.put("data", "");
            return jsonObject;
        }
        mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
        if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
            // 保存邮件信息
            saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_TEMPLATE);
        } else {
            // 修改邮件信息
            updateMailInfo(mailModel);
        }
        jsonObject.put("code", 200);
        jsonObject.put("msg", "成功");
        jsonObject.put("data", "");
        return jsonObject;
    }

    @Override
    public JSONObject sendTemplateAttrMail(MailInfoModel mailModel) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 校验基本邮件信息
        ResultModel resultModel = checkBaseMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_TEMPLATE);
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
            /*if (mailModel.getAttachmentPathArr() != null) {
                // 添加多个文件
                for (String attachmentPath : mailModel.getAttachmentPathArr()) {
                    String fileName = attachmentPath.substring(attachmentPath.lastIndexOf(File.separator) + 1);
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachmentPath));
                    mimeMessageHelper.addAttachment(fileName, fileSystemResource);
                }
            }*/


            // 方法1：获取文件
            //File file = org.springframework.util.ResourceUtils.getFile("classpath:test.txt");
            // 方法2：获取文件或流
            //ClassPathResource classPathResource = new ClassPathResource("test.txt");
            //classPathResource .getFile();
            //classPathResource .getInputStream();

            // ----------上面的方法在window上可以，下面方法可以读取jar包下的文件
            // ----------首先获得当前的类加载器，通过类加载器读取文件。

            // 方法1
            //InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("files" + File.separator + "泰照护机构运营管理平台.pdf");
            // 方法2
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.txt");


            // 第一种 使用inputStream
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(File.separator + "files" + File.separator + "泰照护机构运营管理平台.pdf");
            //mimeMessageHelper.addAttachment("cesi.pdf", new ByteArrayResource(IOUtils.toByteArray(inputStream)));

            // 第二种 inputStream转换为ByteArrayOutputStream
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(File.separator + "files" + File.separator + "泰照护机构运营管理平台.pdf");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024*4];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            mimeMessageHelper.addAttachment("泰照护机构运营管理平台.pdf", new ByteArrayResource(output.toByteArray()));

            mimeMessageHelper.setSentDate(new Date());
            mailSender.send(mimeMessage);
            logger.info("发送邮件成功");
        } catch (Exception e) {
            if (mailModel.getRetryNumber() < 2) {
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
            jsonObject.put("msg", "系统异常，请稍后重试");
            jsonObject.put("data", "");
            return jsonObject;
        }
        mailModel.setSendState(MailSendStateConstant.MAIL_SEND_SUCCESS);
        if (StringUtil.isNullOrEmpty(mailModel.getMailId())) {
            // 保存邮件信息
            saveMailInfo(mailModel, MailTypeConstant.MAIL_TYPE_TEMPLATE);
        } else {
            // 修改邮件信息
            updateMailInfo(mailModel);
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
    public ResultModel checkBaseMailInfo(MailInfoModel mailModel, Integer mailType) throws Exception {
        if (StringUtil.isNullOrEmpty(from)) {
            return ResultModel.error(ExceptionEnum.EMAIL_FROM_NOT_NULL_EXCEPTION.getMessage());
        }
        if (mailModel.getToArr() == null) {
            return ResultModel.error(ExceptionEnum.EMAIL_TO_NOT_NULL_EXCEPTION.getMessage());
        }
        if (StringUtil.isNullOrEmpty(mailModel.getSubject())) {
            return ResultModel.error(ExceptionEnum.EMAIL_SUBJECT_NOT_NULL_EXCEPTION.getMessage());
        }
        if (mailType != MailTypeConstant.MAIL_TYPE_TEMPLATE) {
            if (StringUtil.isNullOrEmpty(mailModel.getContent())) {
                return ResultModel.error(ExceptionEnum.EMAIL_CONTENT_NOT_NULL_EXCEPTION.getMessage());
            }
        }
        return ResultModel.ok();
    }

    /**
     * 保存邮件信息
     *
     * @param mailModel
     */
    public ResultModel saveMailInfo(MailInfoModel mailModel, Integer mailType) throws Exception {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MailInfoDao mailInfoDao = new MailInfoDao();
        mailInfoDao.setMailId(uuid);
        mailInfoDao.setMailFrom(from);
        mailInfoDao.setMailTo(StringUtils.join(mailModel.getToArr(), ","));
        if (mailModel.getCcArr() != null) {
            mailInfoDao.setMailCc(StringUtils.join(mailModel.getCcArr(), ","));
        }
        if (mailModel.getBccArr() != null) {
            mailInfoDao.setMailBcc(StringUtils.join(mailModel.getBccArr(), ","));
        }
        mailInfoDao.setSubject(mailModel.getSubject());
        mailInfoDao.setContent(mailModel.getContent());
        if (mailModel.getPictureMap() != null) {
            String pictureJson = JSONObject.toJSONString(mailModel.getPictureMap());
            mailInfoDao.setPictureJson(pictureJson);
        }
        if (mailModel.getAttachmentPathArr() != null) {
            mailInfoDao.setAttachmentPath(StringUtils.join(mailModel.getAttachmentPathArr(), "|"));
        }
        mailInfoDao.setTemplateName(mailModel.getTemplateName());
        if (mailModel.getTemplateMap() != null) {
            String templateJson = JSONObject.toJSONString(mailModel.getTemplateMap());
            mailInfoDao.setTemplateJson(templateJson);
        }
        mailInfoDao.setSendDate(new Date());
        mailInfoDao.setMailType(mailType);
        mailInfoDao.setSendState(mailModel.getSendState());
        mailInfoDao.setDeleteState(MailDeleteStateConstant.MAIL_INFO_NOT_DELETED);
        int count = mailInfoMapper.addMailInfo(mailInfoDao);
        if (count != 1) {
            return ResultModel.error(ExceptionEnum.EMAIL_SAVE_FAIL_EXCEPTION.getMessage());
        }
        return ResultModel.ok(mailInfoDao.getMailId());
    }

    /**
     * 修改邮件状态
     *
     * @param mailModel
     * @throws Exception
     */
    public ResultModel updateMailInfo(MailInfoModel mailModel) throws Exception {
        MailInfoDao mailInfoDao = mailInfoMapper.getMailInfoByMailId(mailModel.getMailId());
        mailInfoDao.setSendState(mailModel.getSendState());
        mailInfoDao.setSendDate(new Date());
        int count = mailInfoMapper.updateMailInfo(mailInfoDao);
        if (count != 1) {
            return ResultModel.error(ExceptionEnum.EMAIL_UPDATE_FAIL_EXCEPTION.getMessage());
        }
        return ResultModel.ok(mailInfoDao.getMailId());
    }
}