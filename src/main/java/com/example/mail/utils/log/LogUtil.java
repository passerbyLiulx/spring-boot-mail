package com.example.mail.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    static Logger processLogger = LoggerFactory.getLogger("PROCESS");
    static Logger businessLogger = LoggerFactory.getLogger("BUSINESS");

    public LogUtil() {
    }

    public static void process(String massage, Object... keys) {
        processLogger.info(massage, keys);
    }

    public static void business(String massage, Object... keys) {
        businessLogger.info(massage, keys);
    }

}
