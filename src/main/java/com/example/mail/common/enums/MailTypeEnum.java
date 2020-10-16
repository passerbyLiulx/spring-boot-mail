package com.example.mail.common.enums;

/**
 * 邮件类型
 */
public enum MailTypeEnum {

    MAIL_TYPE_SIMPLE(1, "简单文本邮件"),

    MAIL_TYPE_INLINE(2, "带图片邮件"),

    MAIL_TYPE_ATTACHMENT(3, "带附件邮件"),

    MAIL_TYPE_TEMPLATE(4, "带模板邮件");

    private int code;

    private String massage;

    MailTypeEnum(int code, String massage) {
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
