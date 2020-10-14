package com.example.mail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MailInfoModel {

    @ApiModelProperty(value = "发送人")
    private String mailId;

    @ApiModelProperty(value = "发送人")
    private String mailFrom;

    @ApiModelProperty(value = "接收人")
    private String[] toArr = null;

    @ApiModelProperty(value = "抄送")
    private String[] ccArr = null;

    @ApiModelProperty(value = "密送")
    private String[] bccArr = null;

    @ApiModelProperty(value = "主题")
    private String subject;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "图片信息 key：类别Id，value：图片路径")
    private Map<String, Object> pictureMap = null;

    @ApiModelProperty(value = "附件路径")
    private String[] attachmentPathArr = null;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "key：参数，value：内容")
    private Map<String, Object> templateMap = null;

    @ApiModelProperty(value = "发送状态")
    private Integer sendState;

    @ApiModelProperty(value = "发送时间")
    private Date sendDate;

    @ApiModelProperty(value = "重试次数")
    private Integer retryNumber = 0;

    public MailInfoModel() {

    }

    /**
     * 基本参数
     * @param toArr 接收人
     * @param ccArr 抄送人
     * @param bccArr 密送人
     * @param subject 主题
     * @param content 内容
     */
    public MailInfoModel(String[] toArr, String[] ccArr, String[] bccArr, String subject, String content) {
        this.toArr = toArr;
        this.ccArr = ccArr;
        this.bccArr = bccArr;
        this.subject = subject;
        this.content = content;
    }
}
