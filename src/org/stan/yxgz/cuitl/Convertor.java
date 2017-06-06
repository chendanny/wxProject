package org.stan.yxgz.cuitl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Convertor {
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List translate(List values, Class destClass) {
		List destList = new ArrayList();
		Object dest;
		for (int i=0; i<values.size(); i++) {
			dest = translate(values.get(i), destClass);
			destList.add(dest);
		}
		return destList;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object translate(Object value, Class destClass)
		throws ServiceError
	{
		if (value==null)
		{
			return null;
		}
		if (value.getClass()==destClass) {
			return value;
		}
		String className = destClass.getName();
		if (className.equals("java.lang.String"))
		{
			value = value.toString();
		}
		else if (className.equals("java.lang.Integer"))
		{
			if (value instanceof String )
			{
				String str = (String) value;
				if (str.equals("")) {
					value = null;
				}
				else {
					if (str.indexOf('.')>0) {
						str = str.substring(0, str.indexOf('.'));
					}
					value = new Integer(str);
				}
			}
			else if (value instanceof BigDecimal) {
				value = ((BigDecimal)value).intValue();
			}
		}
		else if (className.equals("java.lang.Long")) {
			if (value instanceof String )
			{
				if (value.equals("")) {
					value = null;
				}
				else {
					value = new Long((String)value);
				}
			}
			else if (value instanceof BigDecimal) {
				value = ((BigDecimal)value).longValue();
			}
		}
		else if (className.equals("java.lang.Double")) 
		{
			//System.out.println("####### "+value.getClass().getName());
			if (value instanceof String) {
				if (value.equals("")) {
					value = null;
				}
				else {
					value = new Double((String)value);
				}
			}
			else if (value instanceof Float) {
				Float f = (Float)value;
				value = f.doubleValue();
			}
			else if (value instanceof Integer) {
				value = new Double(value.toString());
			}
			else if (value instanceof BigDecimal) {
				BigDecimal bd = (BigDecimal)value;
				value = bd.doubleValue();
			}
 		}
		else if (className.equals("java.lang.Float")) {
			if (value instanceof String) {
				value = new Float((String)value);
			}
			else if (value instanceof Double) {
				Double d = (Double)value;
				value = d.floatValue();
			}
			else if (value instanceof Integer) {
				value = new Float(value.toString());
			}
			else if (value instanceof BigDecimal) {
				BigDecimal bd = (BigDecimal)value;
				value = bd.floatValue();
			}
		}
		else if (className.equals("java.sql.Timestamp")) {
			if (value instanceof String) {
				Timestamp time = toTimestamp((String)value);
				return time;
			}
			else if ((value instanceof Date)) {
				Timestamp time = new Timestamp(((Date)value).getTime());
				return time;
			}
		}
		return value;
	}
	
	
	public static Integer toInteger(Object value) {
		return (Integer) translate(value, Integer.class);
	}
	
	public static String toString(Object value) {
		return (String) translate(value, String.class);
	}

	public static Timestamp toTimestamp(String dateStr) {
		if (dateStr==null) {
			return null;
		}
		Date d = DateUtil.strToDate(dateStr);
		return d==null?null:new Timestamp(d.getTime());
	}
	
	public static boolean toBool(String s) {
		if (s!=null) {
			s = s.trim().toLowerCase();
			if (s.equals("1") 
					|| s.equals("t") ||s.equals("true")
					|| s.equals("y") || s.equals("yes") ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean equals(Object value1, Object value2) {
		if (value1==null||value2==null) {
			return false;
		}
		String s1 = toString(value1);
		String s2 = toString(value2);
		return s1.equals(s2);
	}
	
}
