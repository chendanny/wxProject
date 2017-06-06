package org.stan.yxgz.action;

import java.util.Calendar;
import java.util.Date;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] months=new String[]{"3","6","9","12"};
		int aa=getSeason(new Date());
		Calendar c = Calendar.getInstance();  
		int month = c.get(Calendar.MONTH) + 1;
		System.out.println(aa);
	}
	
	
	public static int getSeason(Date date) {  
		  
        int season = 0;  
  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        int month = c.get(Calendar.MONTH);  
        switch (month) {  
        case Calendar.JANUARY:  
        case Calendar.FEBRUARY:  
        case Calendar.MARCH:  
            season = 3;  
            break;  
        case Calendar.APRIL:  
        case Calendar.MAY:  
        case Calendar.JUNE:  
            season = 3;  
            break;  
        case Calendar.JULY:  
        case Calendar.AUGUST:  
        case Calendar.SEPTEMBER:  
        case Calendar.OCTOBER:  
            season = 2;  
            break;  
        case Calendar.NOVEMBER:  
        case Calendar.DECEMBER:  
            season =2;  
            break;  
        default:  
            break;  
        }  
        return season;  
    }  
}
