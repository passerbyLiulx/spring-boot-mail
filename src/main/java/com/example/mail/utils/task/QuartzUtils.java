package com.example.mail.utils.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * 定时任务工具
 *
 */
public final class QuartzUtils {

	private final static Logger log = LoggerFactory.getLogger(QuartzUtils.class);

	/**
	 * 获取一次性定时表达式
	 * @param year
	 * @param month
	 * @param date
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static String getOnceQuartzExpression(int year,int month,int date,int hours,int minutes,int seconds){
		return seconds+" "+minutes+" "+hours+" "+date+" "+month+" ? "+year;
	}
	
	public static String getOnceQuartzExpression(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return getOnceQuartzExpression(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE),c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
	}
	
	public static String getOnceQuartzExpression(Calendar c){
		return getOnceQuartzExpression(c.getTime());
	}
	
	/*public static String getOnceQuartzExpression(String dateStr){
		return getOnceQuartzExpression(DateUtil.parseDate(dateStr));
	}*/
	
	/**
	 * 按天表达式
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param weeks
	 * @return
	 */
	public static String getDayQuartzExpression(int hours,int minutes,int seconds,int weeks[]){
		String exp = seconds+" "+minutes+" "+hours+" ? * ";
		if(weeks!=null && weeks.length!=0){
			for(int i=0;i<weeks.length;i++){
				exp+=weeks[i]+"";
				if(i!=weeks.length-1){
					exp+=",";
				}
			}
		}else{
			exp+="1-7 ";
		}
		exp+="*";
		return exp;
	}
	
	/**
	 * 按周表达式
	 * @param week
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static String getWeekQuartzExpression(int week,int hours,int minutes,int seconds){
		return seconds+" "+minutes+" "+hours+" ? * "+week+" *";
	}
	
	/**
	 * 按月定时表达式
	 * @param month
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static String getMonthQuartzExpression(int month,int hours,int minutes,int seconds){
		return seconds+" "+minutes+" "+hours+" ? "+month+" 1-7 *";
	}
	
	/**
	 * 按年定时表达式
	 * @param month
	 * @param date
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static String getYearQuartzExpression(int month,int date,int hours,int minutes,int seconds){
		return seconds+" "+minutes+" "+hours+" "+date+" "+month+" ? *";
	}
	
	/**
	 * 提前提醒
	 * @param year
	 * @param month
	 * @param date
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static String getPreRemindQuartzExpression(int year,int month,int date,int hours,int minutes,int seconds,int preDays,int preHours,int preMinutes){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DATE, date);
		c.set(Calendar.HOUR_OF_DAY, hours);
		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.SECOND, seconds);
		
		c.add(Calendar.DATE, -preDays);
		c.add(Calendar.HOUR, -preHours);
		c.add(Calendar.MINUTE, -preMinutes);
		
		return c.get(Calendar.SECOND)+" "+c.get(Calendar.MINUTE)+" "+c.get(Calendar.HOUR_OF_DAY)+" "+c.get(Calendar.DATE)+" "+(c.get(Calendar.MONTH)+1)+" ? "+c.get(Calendar.YEAR);
	}
}
