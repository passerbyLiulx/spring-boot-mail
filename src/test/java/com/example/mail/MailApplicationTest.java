package com.example.mail;

import com.example.mail.model.MailInfoModel;
import com.example.mail.service.MailInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MailApplicationTest {

    @Autowired
    private MailInfoService mailService;

    /**
     * 简单文本邮件
     */
    @Test
    public void sendSimpleMailTest() throws Exception {
        String[] toArr = new String[]{"1191026928@qq.com"};
        //String[] ccArr = new String[]{"1099020655@qq.com","1370916560@qq.com"};
        String subject = "SpringBootMail实现邮件发送";
        String content = "SpringBootMail实现邮件发送正文";
        MailInfoModel mailModel = new MailInfoModel(toArr, null, null, subject, content);
        mailService.sendSimpleMail(mailModel);
    }

    /**
     * 带图片邮件
     */
    @Test
    public void sendInlineMailTest() throws Exception {
        String[] toArr = new String[]{"1191026928@qq.com"};
        String subject = "SpringBootMail实现邮件发送";
        String content = "<html><body><img src='cid:ceshi1'><img src='cid:ceshi2'>SpringBootMail实现邮件发送正文</body></html>";
        MailInfoModel mailModel = new MailInfoModel(toArr, null, null, subject, content);
        Map<String, Object> pictureMap = new HashMap<>();
        pictureMap.put("ceshi1", "D:\\项目测试\\测试1.png");
        pictureMap.put("ceshi2", "D:\\项目测试\\测试2.png");
        mailModel.setPictureMap(pictureMap);
        mailService.sendInlineMail(mailModel);
    }

    /**
     * 带附件邮件
     */
    @Test
    public void sendAttachmentMailTest() throws Exception {
        String[] toArr = new String[]{"llx1191026928@163.com"};
        //String subject = "SpringBootMail实现邮件发送";
        String subject = "泰照护机构运营管理平台使用申请书";
        //String content = "SpringBootMail实现邮件发送正文";
        String content = "尊敬的客户：\n" +
                "  您好！感谢您注册使用泰照护机构运营管理平台。\n" +
                "  由于在申请注册流程中，需上传加盖公章的《泰照护机构运营管理平台使用申请书》原件照片或扫描件，所以请您先下载附件中的《申请书模版》并填写，填写完成后再进行注册。\n" +
                "\n" +
                "泰照护机构运营管理平台\n" +
                "2020-09-22";
        MailInfoModel mailModel = new MailInfoModel(toArr, null, null, subject, content);
        mailModel.setAttachmentPathArr(new String[]{"D:\\项目测试\\泰照护机构运营管理平台使用申请书.pdf", "D:\\项目测试\\测试2.txt"});
        // 取消切割，默认是true
        System.setProperty("mail.mime.splitlongparameters", "false");
        mailService.sendAttachmentMail(mailModel);
    }

    /**
     * 模板邮件
     */
    @Test
    public void sendTemplateMailTest() throws Exception {
        String[] toArr = new String[]{"1191026928@qq.com"};
        String subject = "SpringBootMail实现邮件发送";
        String content = "SpringBootMail实现邮件发送正文";
        MailInfoModel mailModel = new MailInfoModel(toArr, null, null, subject, content);
        mailModel.setTemplateName("mailTemplate");
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("title", "邮件测试");
        templateMap.put("content", "邮件测试成功了,/;;\",,,\";");
        mailModel.setTemplateMap(templateMap);
        //mailModel.setTemplateName("registerMailTemplate");
        mailModel.setAttachmentPathArr(new String[]{"D:\\项目测试\\泰照护机构运营管理平台使用申请书.pdf", "D:\\项目测试\\测试2.txt"});
        // 取消切割，默认是true
        System.setProperty("mail.mime.splitlongparameters", "false");
        mailService.sendTemplateMail(mailModel);
    }
}
