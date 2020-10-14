package com.example.mail.controller;

import com.example.mail.model.MailInfoModel;
import com.example.mail.model.Result;
import com.example.mail.service.MailInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mailController")
@Api(tags = "邮件服务")
public class MailInfoController {

    @Autowired
    private MailInfoService mailService;

    /**
     * 简单文本邮件
     * @param mailModel
     * @return
     */
    @PostMapping("/sendSimpleMail")
    @ApiOperation("简单文本邮件")
    public Result sendSimpleMail(@RequestBody MailInfoModel mailModel) {
        try {
            mailService.sendSimpleMail(mailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error();
        }
        return  Result.ok();
    }

    /**
     * 带图片邮件
     * @param mailModel
     * @return
     */
    @PostMapping("/sendInlineMail")
    @ApiOperation("带图片邮件")
    public Result sendInlineMail(@RequestBody MailInfoModel mailModel) {
        try {
            mailService.sendInlineMail(mailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error();
        }
        return  Result.ok();
    }

    /**
     * 带附件邮件
     * @param mailModel
     * @return
     */
    @PostMapping("/sendAttachmentMail")
    @ApiOperation("带附件邮件")
    public Result sendAttachmentMail(@RequestBody MailInfoModel mailModel) {
        try {
            mailService.sendAttachmentMail(mailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error();
        }
        return  Result.ok();
    }

    /**
     * 模板邮件
     * @param mailModel
     * @return
     */
    @PostMapping("/sendTemplateMail")
    @ApiOperation("模板邮件")
    public Result sendTemplateMail(@RequestBody MailInfoModel mailModel) {
        try {
            mailService.sendTemplateMail(mailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error();
        }
        return  Result.ok();
    }
}
