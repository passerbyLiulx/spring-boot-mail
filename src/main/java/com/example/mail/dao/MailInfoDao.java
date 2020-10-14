package com.example.mail.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MailInfoDao implements Serializable {

    /**
     * 邮件Id
     */
    private String mailId;

    /**
     * 发送人 以逗号间隔
     */
    private String mailFrom;

    /**
     * 接收人 以逗号间隔
     */
    private String mailTo;

    /**
     * 抄送人 以逗号间隔
     */
    private String mailCc;

    /**
     * 密送人 以逗号间隔
     */
    private String mailBcc;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * key：类别Id，value：图片路径
     */
    private String pictureJson;

    /**
     * 附件路径 以符号 ”|“ 间隔
     */
    private String attachmentPath;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * key：参数，value：内容
     */
    private String templateJson;

    /**
     * 邮件类型 1：简单文本邮件 2：带图片邮件 3：带附件邮件 4：模板邮件
     */
    private Integer mailType;

    /**
     * 发送状态 0：失败 1：成功
     */
    private Integer sendState;

    /**
     * 发送时间
     */
    private Date sendDate;

    /**
     * 删除状态 0：未删除 1：删除
     */
    private Integer deleteState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
