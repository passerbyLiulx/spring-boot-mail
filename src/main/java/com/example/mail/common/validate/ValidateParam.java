package com.example.mail.common.validate;

import lombok.Data;

@Data
public class ValidateParam {

    public static final String REG_NAME = "[^A-Za-z\\u4E00-\\u9FA5\\•\\·\\_\\-\\s]+|([\\u4E00-\\u9FA5][\\•\\·\\_\\-]{2,}[\\u4E00-\\u9FA5])|([\\u4E00-\\u9FA5][\\s]{1,}[\\u4E00-\\u9FA5])|([A-Za-z][\\•\\·\\_\\-\\s]{2,}[A-Za-z])|([A-Za-z][\\•\\·\\_\\-\\s]{1,}[\\u4E00-\\u9FA5])|([\\u4E00-\\u9FA5][\\•\\·\\_\\-\\s]{1,}[A-Za-z])";
    public static final String REG_TEL = "^1\\d{10}$";
    public static final String REG_PHONE = "^(0(\\d{2,3})|(\\d{3}\\-))?(0\\d2,3|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$";
    public static final String REG_IDCARD = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    public static final String REG_MAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,50}";

    private String fildName;
    private String regExt;
    private String msg;
    private boolean isFlag;

    private ValidateParam() {
        this.isFlag = false;
    }

    public ValidateParam(String fildName, String regExt, String msg) {
        this(fildName, regExt, msg, false);
    }

    public ValidateParam(String fildName, String regExt, String msg, boolean isFlag) {
        this.isFlag = false;
        this.fildName = fildName;
        this.regExt = regExt;
        this.msg = msg;
        this.isFlag = isFlag;
    }

}
