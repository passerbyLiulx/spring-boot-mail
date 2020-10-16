package com.example.mail.common.enums;

/**
 * 邮件删除状态
 */
public enum MailDeleteStateEnum {

    MAIL_NOT_DELETED(0, "未删除"),

    MAIL_DELETED(1, "已删除");

    private int code;

    private String massage;

    MailDeleteStateEnum(int code, String massage) {
        this.code = code;
        this.massage = massage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
