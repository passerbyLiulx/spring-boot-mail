package com.example.mail.service;

import com.example.mail.model.MailInfoModel;

public interface MailInfoService {

    /**
     * 简单文本邮件
     * @param mailModel
     */
    public void sendSimpleMail(MailInfoModel mailModel) throws Exception;

    /**
     * 带图片邮件
     * @param mailModel
     */
    public void sendInlineMail(MailInfoModel mailModel) throws Exception;

    /**
     * 带附件邮件
     * @param mailModel
     */
    public void sendAttachmentMail(MailInfoModel mailModel) throws Exception;

    /**
     * 带模板邮件
     * @param mailModel
     */
    public void sendTemplateMail(MailInfoModel mailModel) throws Exception;

}
