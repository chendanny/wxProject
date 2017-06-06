package org.stan.yxgz.cuitl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class JsonUtil {

	
	@SuppressWarnings("rawtypes")
	public static String getJSONString(List data, int totalCount) throws Exception 
    {
		String s = toJSONString(data);
		return "{rownum:"+totalCount+", results:"+s+"}";
    }	
    
	@SuppressWarnings("rawtypes")
	public static String toJSONString(List data) throws Exception 
    {
    	if (data==null || data.size()==0) {
    		return "[]" ;
    	}
    	StringBuffer sb = new StringBuffer("[");
    	for (int i=0; i<data.size(); i++) {
    		if (i>0) {
    			sb.append(",");
    		}
    		sb.append(toJSONString(data.get(i)));
    	}
    	sb.append("]");
    	return sb.toString();
    }	
    
    @SuppressWarnings("rawtypes")
	public static Map<String, Object> objectToValueMap(Object recObj) throws Exception {
    	ClassAnalyzer ca = ClassAnalyzer.getClassAnalyzer(recObj.getClass());
    	List<String> fieldNames = ca.getNormalFields();
    	//Printer.print(fieldNames, "fieldNames");
		HashMap<String, Object> fieldValues = new HashMap<String, Object>();
		Object value;
		String fieldName;
		for (int j=0; j<fieldNames.size(); j++) {
			fieldName = fieldNames.get(j);
			value = getFieldValue(recObj, fieldName);
			if (value!=null) {
				if (value instanceof Map) {
					if (((Map)value).size()==0) {
						continue;
					}
				}
				fieldValues.put(fieldName, value);
			}
		}
		//Printer.print(fieldValues, "fieldValues");
		return fieldValues;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static String toJSONString(Object object) throws Exception {
    	if (object==null) {
    		return "";
    	}
    	Class clazz = object.getClass();
    	if (clazz.getName().startsWith("java.lang")) {
    		if (clazz==String.class) {
    			return "\""+object.toString()+"\"";
    		}
    		else {
        		return object.toString();
    		}
    	}
    	else if (clazz.getName().startsWith("java")) {
    		if (object instanceof Map) {
    			Map temp = (Map)object;
    			if (!temp.isEmpty()) {
        			return toJSONString(temp);
    			}
    		}
    		else if (object instanceof List) {
    			List temp = (List)object;
        		return toJSONString(temp);
    		}
    		return null;
    	}
		Map<String, Object> fieldValues = objectToValueMap(object);
		return toJSONString(fieldValues);
    }
    
	public static String toJSONString(Map<String, Object> map) throws Exception {
    	if (map==null || map.size()==0) {
    		return "{}" ;
    	}
    	
		Object object;
		String valueStr;
		StringBuffer sb = new StringBuffer("{");
		boolean first = true;
		for (String name: map.keySet()) {
			object = map.get(name);
			valueStr = toJSONString(object);
			if (valueStr!=null) {
				if (!first) {
					sb.append(",");
				}
				else {
					first = false;
				}
				//sb.append(name+":"+toJSONString(value));
				sb.append("\""+name+"\":"+toJSONString(object));
			}
		}
		sb.append("}");
		return sb.toString();
		
    }
    
	public static Object getFieldValue(Object object, String fieldName) throws Exception {
		return new ObjectHandler(object).getValue(fieldName);
	}
	
}
