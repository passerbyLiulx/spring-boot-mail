package com.example.mail.common.enums;

public enum ExceptionEnum {

    SUCCESS(200, "成功"),
    FAIL(500, "请求失败，请稍后重试"),
    EMAIL_SAVE_FAIL_EXCEPTION(1, "添加邮件信息失败"),
    EMAIL_UPDATE_FAIL_EXCEPTION(2, "修改邮件信息失败"),
    EMAIL_DELETE_FAIL_EXCEPTION(3, "删除邮件信息失败"),
    EMAIL_SEARCH_FAIL_EXCEPTION(4, "查询邮件信息失败"),
    EMAIL_SEND_SECCESS(5, "发送邮件成功"),
    EMAIL_SEND_FAIL(6, "发送邮件失败"),
    EMAIL_FROM_NOT_NULL_EXCEPTION(21, "发送人不能为空"),
    EMAIL_TO_NOT_NULL_EXCEPTION(22, "接收人不能为空"),
    EMAIL_SUBJECT_NOT_NULL_EXCEPTION(23, "主题不能为空"),
    EMAIL_CONTENT_NOT_NULL_EXCEPTION(24, "内容不能为空"),
    EMAIL_TEMPLATE_NAME_NOT_NULL_EXCEPTION(25, "模板名称不能为空");

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
