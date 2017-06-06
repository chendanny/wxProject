package org.stan.yxgz.cuitl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");

	
	public static String formatDateStr(String s) {
		String formatStr = null;
		if (s.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			formatStr = "yyyy-MM-dd";
		}
		else if (s.matches("\\d{8}")) {
			formatStr = "yyyyMMdd";
		}
		if (formatStr!=null) {
			SimpleDateFormat fmt = new SimpleDateFormat(formatStr);
			Date d;
			try {
				d = fmt.parse(s);
			} catch (ParseException e) {
				return null;
			}
			fmt = new SimpleDateFormat("yyyyMMdd");
			return fmt.format(d);
		}
		return null;
	}
	
	public static String formatTimeStr(String s) {
		if (s==null || !s.matches("\\d{1,2}:\\d{1,2}")) {
			return null;
		}
		int p = s.indexOf(":");
		int hour = Integer.parseInt(s.substring(0,p).trim());
		int minute = Integer.parseInt(s.substring(p+1).trim());
		String hourStr = hour<10?("0"+hour):String.valueOf(hour);
		String minuteStr = minute<10?("0"+minute):String.valueOf(minute);
		return hourStr + minuteStr;
	}
	
	public static Date strToDate(String s) {
		if (s==null) {
			return null;
		}
		
		String formatStr = null;
		if (s.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			formatStr = "yyyy-MM-dd";
		}
		else if (s.matches("\\d{8}")) {
			formatStr = "yyyyMMdd";
		}
		else if (s.matches("\\d{14}")) {
			formatStr = "yyyyMMddHHmmss";
		}
		else if (s.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}")) {
			formatStr = "yyyy-MM-dd HH:mm";
		}
		
		Date d = null;
		if (formatStr!=null) {
			SimpleDateFormat fmt = new SimpleDateFormat(formatStr);
			try {
				d = fmt.parse(s);
			} catch (ParseException e) {
			}
		}
		return d;
		
	}
	
	public static String getMonthString(Date d) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
		return fmt.format(d);
	}
	public static String getDateString(Date d) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(d);
	}
	
	public static Date parseMonth(String str) { 
		try
		{
			return monthFormat.parse(str);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date addMonth(Date d, int n) {
		String month = monthFormat.format(d);
		int y = Integer.parseInt(month.substring(0, 4));
		int m = Integer.parseInt(month.substring(4));
		System.out.println(">> y="+y+", m="+m);
		int m1 = y*12+m+n;
		y = (m1-1)/12;
		m = (m1-1)%12+1;
		System.out.println("y="+y+", m="+m);
		month = formatInt(y, 4)+formatInt(m, 2);
		try {
			return monthFormat.parse(month);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatInt(int n, int len) {
		
		String temp = "000000000000000000000000";
		String s1 = String.valueOf(n);
		if (s1.length()<len) {
			s1 = temp.substring(0, len-s1.length())+s1;
		}
		return s1;
		
	}
	
}
