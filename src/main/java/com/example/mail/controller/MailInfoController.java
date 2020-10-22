package com.example.mail.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mail.model.MailInfoModel;
import com.example.mail.model.ResultModel;
import com.example.mail.service.MailInfoService;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mailController")
@Api(tags = "邮件服务")
public class MailInfoController {

    @Autowired
    private MailInfoService mailService;

    /**
     * 简单文本邮件
     * @param mailInfoModel
     * @return
     */
    @PostMapping("/sendSimpleMail")
    @ApiOperation("简单文本邮件")
    @ResponseBody
    public ResultModel sendSimpleMail(@RequestBody MailInfoModel mailInfoModel) throws Exception {
        return mailService.sendSimpleMail(mailInfoModel);
    }

    /**
     * 带图片邮件
     * @param mailInfoModel
     * @return
     */
    @PostMapping("/sendInlineMail")
    @ApiOperation("带图片邮件")
    @ResponseBody
    public ResultModel sendInlineMail(@RequestBody MailInfoModel mailInfoModel) throws Exception {
        return mailService.sendInlineMail(mailInfoModel);
    }

    /**
     * 带附件邮件
     * @param mailInfoModel
     * @return
     */
    @PostMapping("/sendAttachmentMail")
    @ApiOperation("带附件邮件")
    @ResponseBody
    public ResultModel sendAttachmentMail(@RequestBody MailInfoModel mailInfoModel) throws Exception {
        return mailService.sendAttachmentMail(mailInfoModel);
    }

    /**
     * 模板邮件
     * @param mailInfoModel
     * @return
     */
    @PostMapping("/sendTemplateMail")
    @ApiOperation("模板邮件")
    public JSONObject sendTemplateMail(@RequestBody MailInfoModel mailInfoModel) throws Exception {
        return mailService.sendTemplateMail(mailInfoModel);
    }


}
