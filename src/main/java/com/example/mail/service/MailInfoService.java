package com.example.mail.service;

import com.example.mail.model.MailInfoModel;
import com.example.mail.model.ResultModel;

public interface MailInfoService {

    /**
     * 简单文本邮件
     * @param mailModel
     */
    public ResultModel sendSimpleMail(MailInfoModel mailModel) throws Exception;

    /**
     * 带图片邮件
     * @param mailModel
     */
    public ResultModel sendInlineMail(MailInfoModel mailModel) throws Exception;

    /**
     * 带附件邮件
     * @param mailModel
     */
    public ResultModel sendAttachmentMail(MailInfoModel mailModel) throws Exception;

    /**
     * 带模板邮件
     * @param mailModel
     */
    public ResultModel sendTemplateMail(MailInfoModel mailModel) throws Exception;

}
