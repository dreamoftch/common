package com.tch.test.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

	public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当天0时时间（eg: 2015-09-01 00:00:00）
	 * 
	 * @return
	 */
	public static Date getStartTimeOfToday() {
		return getStartTime(new Date());
	}

	/**
	 * 获取指定日期的0时时间（eg: 2015-09-01 00:00:00）
	 * 
	 * @return
	 */
	public static Date getStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取指定日期的0时时间（eg: 2015-09-01 00:00:00）
	 * 
	 * @return
	 */
	public static Date getStartTimeOfNextDay() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return getStartTime(cal.getTime());
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long getDateDiff(Date startTime, Date endTime) {
		if (startTime == null || endTime == null) {
			throw new IllegalArgumentException("startTime/stopTime不能为空");
		}
		startTime = getStartTime(startTime);
		long start = startTime.getTime();
		long stop = endTime.getTime();
		long dayDuration = 24 * 60 * 60 * 1000;
		return (stop - start) / dayDuration;
	}

	public static String formatDate(Date date) {
		return formatDate(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String formatDate(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.format(date);
		}
		return null;
	}

	public static Date parse(String date) throws ParseException {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return format.parse(date);
		}
		return null;
	}
	
	public static Date parse(String date, String pattern) throws ParseException {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.parse(date);
		}
		return null;
	}

	public static Date parseZeroDate(String date) {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int calDiffMonth(Date startDate, Date endDate) {
		int diff = 0;
		if (startDate == null || endDate == null) {
			return diff;
		}
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDate);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(endDate);
		diff = Math.abs((calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR)) * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.MONTH));
		return diff;
	}

	public static Date addDay(Date date, int day) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static Date getZeroDate(Date date) {
		if (date == null) {
			return date;
		}
		Date zero = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(date));
		try {
			zero = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return zero;
	}

	public static Date getYesterday(Date date) {
		if (date == null) {
			return date;
		}
		Date yesterday = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.DATE, -1);
		yesterday = cal.getTime();
		return yesterday;
	}
	
	public static int getFullYear(Date date){
		if(date ==null){
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获取指定年的第一天
	 * @param year
	 * @return
	 */
	public static Calendar getStartTimeOfYear(int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal;
	}
	
	public static void main(String[] args) {
		System.out.println(formatDate(getStartTimeOfYear(2014).getTime()));
	}

}
