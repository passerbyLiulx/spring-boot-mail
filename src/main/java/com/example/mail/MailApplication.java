package com.example.mail;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class MailApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
        // 取消切割，默认是true
        System.setProperty("mail.mime.splitlongparameters", "false");
    }

}
