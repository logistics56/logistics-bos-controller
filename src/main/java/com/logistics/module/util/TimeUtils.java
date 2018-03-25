package com.logistics.module.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @author lizhen 2018-01-23
 *
 */
public class TimeUtils {

	private TimeUtils() {
		
	}

	
	final static long EIGHT_HOURS = 8 * 60 * 60 * 1000;
	public final static long ONE_HOUR = 60 * 60 * 1000;
	public final static long DAY = 24 * 60 * 60 * 1000;
	public final static long WEEK = 7 * 24 * 60 * 60 * 1000;
	public final static long ONE_MINUTE = 60 * 1000;
	
	public static String getNowDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String mDateTime1=formatter.format(new Date()); 
		return mDateTime1;
	}
	
	public static int getFormatedDate(Long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    return Integer.valueOf(formatter.format(new Date(time)));
	}
	
	public static int getFormatedDateHour(Long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
	    return Integer.valueOf(formatter.format(new Date(time)));
	}
	
	public static String getFormatedDateTime(Long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return formatter.format(new Date(time));
	}
	
	public static String getFormatedDateTime(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	    return formatter.format(time);
	}

	public static int getFormatedMonth(Long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
	    return Integer.valueOf(formatter.format(new Date(time)));
	}

	public static int getFormatedLastMonth(Long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(new Date(time));//设置当前日期
		calendar.add(Calendar.MONTH, -1);//月份减一
	    return Integer.valueOf(formatter.format(calendar.getTime()));
	}

	public static int getWeekDay(long time) {
		Date date=new Date(time);
//		date.setTime(System.currentTimeMillis() - 2 * DAY);
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		dateFm.format(date);
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	public static String dateToString(Date date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String dateToStringInSec(long time) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = new Date(time);
		return sdf.format(date);
	}

	public static int getCurHour() {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date());
	     return ca.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getHourByTime(long time) {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date(time));
	     return ca.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinuteByTime(long time) {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date(time));
	     return ca.get(Calendar.MINUTE);
	}
	
	public static int getSecondByTime(long time) {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date(time));
	     return ca.get(Calendar.SECOND);
	}
	
	public static int getMillsecondByTime(long time) {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date(time));
	     return ca.get(Calendar.MILLISECOND);
	}
	
	public static String getCurYear() {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date());
	     String year = ""+ca.get(Calendar.YEAR);
	     return year;   
	}

	public static String getCurMonth() {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date());
	     String month = ""+ca.get(Calendar.MONTH);
	     return month;   
	}

	public static String getCurDay() {
	     Calendar ca = Calendar.getInstance();
	     ca.setTime(new java.util.Date());
	     String month = ""+ca.get(Calendar.DAY_OF_MONTH);
	     return month; 
	}

	public static long getStartOfDay(long t) {
		return t - (t + EIGHT_HOURS) % DAY;
	}

	public static long getEndOfDay(long t) {
		return t - (t + EIGHT_HOURS) % DAY + DAY;
	}
	
	public static int getCurHourOfDay() {
		long now = System.currentTimeMillis() + EIGHT_HOURS;
		int h = (int)(now % DAY / ONE_HOUR);
		return h;
	}

	public static long getCurHourUnixTime() {
		long now = System.currentTimeMillis();
		return now - (now % ONE_HOUR);
	}
	
	public static long getFirstDayThisMonth() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DATE, 1);
		ca.set(Calendar.HOUR, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}
	
	public static long getFirstDayLastMonth() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MONTH, ca.get(Calendar.MONTH)-1);
		ca.set(Calendar.DATE, 1);
		ca.set(Calendar.HOUR, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}

	public static long getFirstDayNextMonth() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MONTH, ca.get(Calendar.MONTH)+1);
		ca.set(Calendar.DATE, 1);
		ca.set(Calendar.HOUR, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}
	
	public static long getUnixTimeFromFormatedDate(int date) {
		Calendar gc=Calendar.getInstance();
	    try {
			gc.setTime(new SimpleDateFormat("yyyyMMdd").parse(String
					.valueOf(date)));
			return gc.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0L;
		}
//		System.out.println(gc.getTimeInMillis());
	}
	
	/**
	 * 根据时间获取当月多少天
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static int getDayByCurMon(long time) {
		int day = 0;
		try {
			Date date = new Date(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			return day;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}
	
	/**
	 * 对时间进行天加减运算
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static long addDate(long time, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		cal.add(Calendar.DATE, num);
		return cal.getTime().getTime();
	}

	/**
	 * 对小时加减运算
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static long addHour(long time, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		cal.add(Calendar.HOUR_OF_DAY, num);
		return cal.getTime().getTime();
	}
	
	/**
	 * 对分钟加减运算
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static long addMinute(long time, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		cal.add(Calendar.MINUTE, num);
		return cal.getTime().getTime();
	}
	
	public static void main(String[] args) {
//		long t = System.currentTimeMillis();
//		long end = t - ((TimeUtils.getWeekDay(t) - 7) + 7) % 7 * TimeUtils.DAY;
//		System.out.println(getWeekDay(System.currentTimeMillis() - TimeUtils.DAY));
//		System.out.println(getWeekDay(System.currentTimeMillis() - TimeUtils.DAY));
//		Calendar gc=Calendar.getInstance();
//	    try {
//			gc.setTime(new SimpleDateFormat("yyyyMMdd").parse(String
//					.valueOf(20140303)));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		System.out.println(gc.getTimeInMillis());
		System.out.println(getFormatedDateTime(new Date()));
		
//		System.out.println(getFormatedLastMonth(System.currentTimeMillis()));
//		System.out.println(getCurHour());
//		System.out.println(getCurHourOfDay());
//		Calendar ca = Calendar.getInstance();
////		ca.setTime(new java.util.Date());
//		
//		System.out.println(ca.get(Calendar.MONTH));
//		ca.set(Calendar.MONTH, ca.get(Calendar.MONTH)-1);
//		ca.set(Calendar.DATE, 1);
//		ca.set(Calendar.HOUR, 0);
//		ca.set(Calendar.MINUTE, 0);
//		ca.set(Calendar.MILLISECOND, 0);
//		System.out.println(getFormatedDate(ca.getTimeInMillis()));
//		System.out.println(ca.getTimeInMillis());
		
//		System.out.println(getWeekDay());
//		long now = System.currentTimeMillis();
//		for(int i = 0; i < 1000; i++) {
//			Calendar ca = Calendar.getInstance();
//			ca.setTimeInMillis(now);
////			System.out.println(ca.get(Calendar.HOUR_OF_DAY));
//		}
//		System.out.println("cost : " + (System.currentTimeMillis() - now));
//		long now1 = System.currentTimeMillis();
//		for(int i = 0; i < 1000; i++) {
//			now = now + EIGHT_HOURS;
//			long h = now % DAY / (60 * 60 * 1000);
//	//		System.out.println(h);
//		}
//		System.out.println("cost : " + (System.currentTimeMillis() - now1));
	}
}
