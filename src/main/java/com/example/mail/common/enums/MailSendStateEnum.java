package com.example.mail.common.enums;

/**
 * 邮件发送状态
 */
public enum MailSendStateEnum {

    MAIL_SEND_FAIL(0, "失败"),

    MAIL_SEND_SUCCESS(1, "成功");

    private int code;

    private String massage;

    MailSendStateEnum(int code, String massage) {
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
