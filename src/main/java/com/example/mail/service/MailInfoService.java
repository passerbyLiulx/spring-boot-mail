package com.example.mail.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mail.model.MailInfoModel;
import com.example.mail.model.ResultModel;
import io.netty.handler.codec.json.JsonObjectDecoder;

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
    public JSONObject sendTemplateMail(MailInfoModel mailModel) throws Exception;


    /**
     * 带模板邮件
     * @param mailModel
     */
    public JSONObject sendTemplateAttrMail(MailInfoModel mailModel) throws Exception;
}
