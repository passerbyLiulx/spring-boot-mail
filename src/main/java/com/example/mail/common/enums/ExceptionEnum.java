package com.example.mail.common.enums;

public enum ExceptionEnum {

    SUCCESS(200, "成功"),
    EMAIL_FROM_NOT_NULL_EXCEPTION(101, "发送人不能为空"),
    EMAIL_TO_NOT_NULL_EXCEPTION(102, "接收人不能为空"),
    EMAIL_SUBJECT_NOT_NULL_EXCEPTION(103, "主题不能为空"),
    EMAIL_CONTENT_NOT_NULL_EXCEPTION(104, "内容不能为空"),
    EMAIL_TEMPLATE_NAME_NOT_NULL_EXCEPTION(105, "模板名称不能为空");

    private int code;

    private String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
