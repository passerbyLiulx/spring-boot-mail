package com.example.enums;

import com.example.mail.common.enums.WeekEnum;

public class WeekEnumTest {
    public static void main(String[] args) {
        System.out.println(WeekEnum.MONDAY);
        System.out.println(WeekEnum.MONDAY.getCode());
        System.out.println(WeekEnum.MONDAY.getName());
        System.out.println(WeekEnum.TUESDAY.getCode());
        System.out.println(WeekEnum.TUESDAY.getName());
    }
}