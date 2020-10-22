package com.example.mail.utils.validate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mail.common.validate.ValidateParam;
import com.example.mail.model.ValidateResult;
import com.example.mail.utils.log.LogUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegValidateUtil {
    private static Log logger = LogFactory.getLog(RegValidateUtil.class);

    public RegValidateUtil() {
    }

    public static ValidateResult validateJSON(JSON json, ValidateParam... validateBeans) {
        ValidateResult re = new ValidateResult();
        if (json instanceof JSONArray) {
            re = validateJSONArray((JSONArray)json, validateBeans);
        } else if (json instanceof JSONObject) {
            re = validateJSONObject((JSONObject)json, validateBeans);
        }

        return re;
    }

    public static boolean validateString(String regExt, String arg) {
        boolean re = false;
        Pattern pattern = Pattern.compile(regExt);
        Matcher matcher = pattern.matcher(arg);
        re = matcher.find();
        LogUtil.process("RegExt:{}   arg:{}    flag={}", new Object[]{regExt, arg, re});
        return re;
    }

    public static int getChineseLength(String name, String endcoding) {
        int len = 0;

        try {
            int j = 0;
            byte[] b_name = name.getBytes(endcoding);

            do {
                short tmpst = (short)(b_name[j] & 240);
                if (tmpst >= 176) {
                    if (tmpst < 192) {
                        j += 2;
                        len += 2;
                    } else if (tmpst != 192 && tmpst != 208) {
                        if (tmpst == 224) {
                            j += 3;
                            len += 2;
                        } else if (tmpst == 240) {
                            short tmpst0 = (short)((short)b_name[j] & 15);
                            if (tmpst0 == 0) {
                                j += 4;
                                len += 2;
                            } else if (tmpst0 > 0 && tmpst0 < 12) {
                                j += 5;
                                len += 2;
                            } else if (tmpst0 > 11) {
                                j += 6;
                                len += 2;
                            }
                        }
                    } else {
                        j += 2;
                        len += 2;
                    }
                } else {
                    ++j;
                    ++len;
                }
            } while(j <= b_name.length - 1);
        } catch (UnsupportedEncodingException var7) {
            logger.error("getChineseLength is error:{}", var7);
        }

        return len;
    }

    private static ValidateResult validateJSONObject(JSONObject json, ValidateParam... validateBeans) {
        ValidateResult re = new ValidateResult();
        if (validateBeans == null) {
            re.setFlag(false);
            re.setMsg("无校验对象");
            return re;
        } else {
            ValidateParam empV = null;
            re.setFlag(true);
            boolean empFlag = false;
            StringBuilder msg = new StringBuilder();

            for(int i = 0; i < validateBeans.length; ++i) {
                empV = validateBeans[i];
                empFlag = validateString(empV.getRegExt(), json.get(empV.getFildName()).toString());
                if (empV.isFlag()) {
                    empFlag = !empFlag;
                }

                re.setFlag(re.isFlag() && empFlag);
                if (!empFlag) {
                    msg.append(",").append(empV.getMsg());
                }
            }

            if (!re.isFlag()) {
                re.setMsg(msg.substring(1).toString());
            }

            return re;
        }
    }

    private static ValidateResult validateJSONArray(JSONArray json, ValidateParam... validateBeans) {
        ValidateResult re = new ValidateResult();
        re.setFlag(true);
        int len = json.size();
        StringBuilder msg = new StringBuilder();
        JSONObject emp = null;
        ValidateResult empRs = null;

        for(int i = 0; i < len; ++i) {
            emp = json.getJSONObject(i);
            empRs = validateJSONObject(emp, validateBeans);
            if (!empRs.isFlag()) {
                re.setFlag(false);
                msg.append(",{第").append(i).append("个对象:").append(empRs.getMsg()).append("}");
            }
        }

        if (!re.isFlag()) {
            re.setMsg(msg.substring(1).toString());
        }

        return re;
    }

    public static void main(String[] args) {
        JSONArray array = new JSONArray();
        JSONObject request = new JSONObject();
        request.put("deputyMobile", "133333333");
        request.put("headPortraitId", "headPortraitId");
        request.put("deputyIdCard", "代理人身份证号");
        request.put("deputyName", "姓名1");
        request.put("sex", "2");
        request.put("address", "家庭住址1");
        request.put("region", "地区1");
        array.add(request);
        System.out.println(getChineseLength("地区1", "utf-8"));
        JSONObject request1 = new JSONObject();
        request1.put("deputyMobile", "1333333333344");
        request1.put("headPortraitId", "headPortraitId");
        request1.put("deputyIdCard", "12345678901234567X");
        request1.put("idCard", "123456789012345675");
        request1.put("deputyName", "毛泽东");
        request1.put("sex", "2");
        request1.put("address", "家庭住址1");
        request1.put("region", "地区1");
        request1.put("mail", "sfs@asf.as");
        request1.put("landlineTelephone", "05373753330");
        array.add(request1);
        ValidateParam[] vs = new ValidateParam[]{new ValidateParam("idCard", "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", "身份证号码格式不对")};
        ValidateResult re = validateJSON(request1, vs);
        System.out.println(re);
    }
}
