package org.stan.yxgz.cuitl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectHandler {
	

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List filter(List list, String fieldName, 
			Object compareValue) throws ServiceError  
	{
		if (list==null) {
			return null;
		}
		else if (list.size()==0) {
			return list;
		}

		ArrayList result = new ArrayList();
		Class clazz = list.get(0).getClass();
		ClassAnalyzer classAnalyzer = new ClassAnalyzer(clazz);
		
		Field field = classAnalyzer.getField(fieldName);
		if (field==null) {
			throw new ServiceError(clazz.getName()+"'s field "+fieldName + " not found!");
		}
		compareValue = Convertor.translate(compareValue, field.getType()); 
		
		Object obj, fieldValue;
		for (int i=0; i<list.size(); i++)
		{
			obj = list.get(i);
			try
			{
				fieldValue = new ObjectHandler(obj).getValue(fieldName);
				if (compareValue!=null && compareValue.equals(fieldValue))
				{
					result.add(obj);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List filter2(List list, String fieldName, Object equalValue) 
			throws Exception {
		List resultList = new ArrayList();
		if (equalValue==null) {
			return resultList;
		}
		
		Object item, value;
		for (int i=0; i<list.size(); i++) {
			 item = list.get(i);
			 value = new ObjectHandler(item).getValue(fieldName);
			 if (equalValue.equals(value)) {
				 resultList.add(item);
			 }
		}
		return resultList;
	}
	
	
	private Object object;
	private ClassAnalyzer classAnalyzer;
	
	public ObjectHandler() {
	}
	
	public ObjectHandler(Object object) {
		this.setObject(object);
	}
	
	public String getStringValue(String fieldName) throws Exception {
		Object value = getValue(fieldName) ;
		return value==null?null:value.toString(); 
	}
	
	public String getStringValue(Field f) throws Exception {
		Object value = getValue(f) ;
		return value==null?null:value.toString(); 
	}
	
	public Object getValue(String fieldName) throws Exception {
		Method getter = classAnalyzer.getGetterMethod(fieldName);
		if (getter!=null) {
			return getter.invoke(object);
		}
		Field field = classAnalyzer.getField(fieldName);
		if (Modifier.isPublic(field.getModifiers())) {
			return field.get(object);
		}
		printLog("field not found: " + fieldName);
		return null;
	}
	
	private void printLog(String s) {
		System.out.println(s);
	}
	
	public void setValue(String fieldName, Object value) throws Exception 
	{
		Field field = classAnalyzer.getField(fieldName);
		if (field!=null) {
			this.setValue(field, value);
		}
	}
	public void setValueSlient(String fieldName, Object value)  
	{
		try {
			setValue(fieldName, value);
		}
		catch(Exception e) {
		}
	}
	
	public void setValueWhenNull(String fieldName, Object value) throws Exception 
	{
		Field field = classAnalyzer.getField(fieldName);
		if (field!=null)
		{
			if (getValue(fieldName)==null) {
				setValue(fieldName, value);
			}
			//System.out.println("setFieldValue: "+field.getName()+", "+value);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setValues(Map valueMap) throws Exception {
		Iterator it = valueMap.keySet().iterator();
		String name;
		while (it.hasNext()) {
			name = (String)it.next();
			setValue(name, valueMap.get(name));
		}
	}
	
	
	public Object getValue(Field field) throws Exception 
	{
		Method getter = classAnalyzer.getGetterMethod(field.getName());
		Object value = null;
		if (getter==null) {
			//new Throwable().printStackTrace();
			printLog("not found get method of field: "+field.getName()+", class:"+object.getClass().getName());
		}
		if(getter!=null)
		{
			value = getter.invoke(object);
		}
		else if (Modifier.isPublic(field.getModifiers()))
		{
			System.out.println(field.getName()+" is public");
			value = field.get(object);
		}
		return value;
	}
	
	public boolean fieldValueEqual(String fieldName, Object value) throws ServiceError {
		try
		{
			Field field = classAnalyzer.getField(fieldName);
			Object fieldValue = this.getValue(fieldName);
			value = Convertor.translate(value, field.getType());
			return value!=null && value.equals(fieldValue);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void setValue(Field field, Object value) throws Exception
	{
		
		Method setter = this.classAnalyzer.getSetterMethod(field);
		try
		{
			value = Convertor.translate(value, field.getType());
			if(setter!=null)
			{
				setter.invoke(object, new Object[]{value});
			}
			else if (field.isAccessible())
			{
				field.set(object, value);
			}
			else {
				printLog("Set field value failed! " +
						"because of no setter method with field " + field.getName());
			}
		}
		catch(ServiceError e1) {
			e1.setFieldName(field.getName());
			throw e1;
		}
		catch(Exception e) {
			e.printStackTrace();
			printLog("field: "+field.getName()+", value="+value+
					", value class = "+value.getClass().getName()+
					", setter="+setter);
			throw e;
		}
		
	}
	
	
	public List<String> getNotNullFieldNames(List<String> baseFieldNames) {
		List<String> fieldNames = new ArrayList<String>();
		//Printer.get().print(baseFieldNames, "fields");
		Object value;
		try
		{
			for (String fieldName: baseFieldNames) {
				value = getValue(fieldName);
				if (value!=null) {
					fieldNames.add(fieldName);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return fieldNames;
	}

	public Object[] getValues(List<String> fields) throws Exception {
		Object[] values = new Object[fields.size()];
		for (int i=0; i<fields.size(); i++) {
			values[i] = getValue(fields.get(i));
		}
		return values;
	}

	public boolean isNull(Field field) {
		try
		{
			return getValue(field)==null;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public void printDetail() {
		
		List<String> fields = this.classAnalyzer.getNormalFields();
		try
		{
			System.out.println("Object's detail of "+object.toString()+":");
			for (String fieldName: fields) {
				System.out.println("  "+fieldName+" = "+this.getStringValue(fieldName));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setObject(Object object) {
		this.object = object;
		if (this.classAnalyzer==null) {
			this.classAnalyzer = new ClassAnalyzer(object.getClass());
		}
	}

}
