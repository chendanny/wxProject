package org.stan.yxgz.cuitl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClassAnalyzer {
	
	private static Map<String, ClassAnalyzer> analyzers = new HashMap<String, ClassAnalyzer>();
	
	public static ClassAnalyzer getClassAnalyzer(Class clazz) {
		String name = clazz.toString(); 
		ClassAnalyzer ca = analyzers.get(name);
		if (ca==null) {
			ca = new ClassAnalyzer(clazz);
			analyzers.put(name, ca);
		}
		return ca;
	}
	
	public static Class getBindingClass(Class c) {
		Class pc = null;
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            if (p[0] instanceof Class) {
                pc = (Class)p[0];
            }
        }
        return pc;
	}
	
	protected Class<?> c;
	private final List<String> normalFields; 
	
	private final Map<String, List<Field>> annoFieldMap = 
			new HashMap<String, List<Field>>();
	private final Map<String, List<String>> annoFieldNameMap = 
			new HashMap<String, List<String>>();

	
	public ClassAnalyzer(Class<?> c) {
		this.c = c;
		
		normalFields = new ArrayList<String>();
		List<String> classFields ;
		Class clazz = c;
		while (!isRootClass(clazz)) {
			classFields = getNormalFields(clazz);
			for (String name: classFields) {
				if (!normalFields.contains(name)) {
					normalFields.add(name);
				}
			}
			clazz = clazz.getSuperclass();
		}
		
		//Printer.print(normalFields, c.getName()+"'s normalFields");
		
	}
	
	public Field getField(String fieldName) {
		if (isNull(fieldName)) {
			return null;
		}
		return getField(c, fieldName);
	}
	
	public static Field getField(Class clazz, String fieldName) {
		if (clazz==null || fieldName==null) {
			return null;
		}
		for (Field f: clazz.getDeclaredFields())
		{
			if (f.getName().equals(fieldName))
			{
				return f;
			}
		}
		return getField(clazz.getSuperclass(), fieldName);
	}
	
	
	public Method getMethod(String methodName) {
		for (Method m: c.getMethods())
		{
			if (m.getName().equals(methodName))
			{
				return m;
			}
		}
		return null;
	}
	
	public static Method getMethod(Class c, String name, 
			Class... parameterTypes) throws Exception {
		Method method = c.getMethod(name, parameterTypes);
		if (method==null) {
			if (c.getSuperclass()==null) {
				return null;
			}
			method = getMethod(c.getSuperclass(), name, parameterTypes);
		}
		return method;
	}
	
	public Method getGetterMethod(String fieldName) {
		String methodName = getGetterName(fieldName);
		try
		{
			Method getter = getMethod(c, methodName);
			return getter;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return null;
		}
	}
	
	public Method getSetterMethod(Field field) {
		String methodName = getSetterName(field.getName());
		try
		{
			Method setter = getMethod(c, methodName, field.getType());
			return setter;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean canFieldRead(String fieldName) {
		Field f = getField(fieldName);
		if (f==null)
		{
			return false;
		}
		if (f.isAccessible())
		{
			return true;
		}
		Method getter = getGetterMethod(fieldName);
		return getter!=null;
	}
	
	public boolean canFieldWrite(String fieldName) {
		Field f = getField(fieldName);
		if (f==null)
		{
			return false;
		}
		if (f.isAccessible())
		{
			return true;
		}
		Method setter = getSetterMethod(f);
		return setter!=null;
	}
	
	private String getSetterName(String fieldName)
	{
		String firstChar = String.valueOf(fieldName.charAt(0));
		fieldName = fieldName.replaceFirst(firstChar, firstChar.toUpperCase());
		return "set"+fieldName;
	}
	
	private String getGetterName(String fieldName)
	{
		String firstChar = String.valueOf(fieldName.charAt(0));
		fieldName = fieldName.replaceFirst(firstChar, firstChar.toUpperCase());
		return "get"+fieldName;
	}

	static boolean isRootClass(Class clazz) {
		return clazz.equals(Object.class);
	}
	
	public List<Field> getAnnotatedFields(Class annoClass) {
		List<Field> fields = annoFieldMap.get(annoClass.getName());
		if (fields==null) {
			fields = new ArrayList<Field>();
			Field field;
			for (String fieldName: normalFields)
			{
				field = this.getField(fieldName);
				if (field.getAnnotation(annoClass)!=null)
				{
					fields.add(field);
				}
			}
		}
		return fields;
	}
	
	public List<String> getAnnotatedFieldNames(Class annoClass) {
		List<String> fields = annoFieldNameMap.get(annoClass.getName());
		if (fields==null) {
			fields = toNameList(this.getAnnotatedFields(annoClass));
		}
		return fields;
	}
	
	public Field getFirstAnnotatedField(Class annoClass) {
		List<Field> fields = getAnnotatedFields(annoClass);
		if (fields==null||fields.size()==0) {
			return null;
		}
		else {
			return fields.get(0);
		}
	}
	
	public boolean fieldExists(Class annoClass) {
		return this.getFirstAnnotatedField(annoClass)!=null;
	}
	public boolean fieldExists(String fieldName) {
		return this.getField(fieldName)!=null;
	}
	
	public boolean isIntegerField(String fieldName) {
		Field field = getField(fieldName);
		if (field==null) {
			return false;
		}
		String typeName = field.getType().getName();
		return typeName.equals("java.lang.Integer");
	}

	public List<String> getNormalFields() {
		return this.normalFields;
	}

	public static List<String> getNormalFields(Class clazz) {
		List<String> fields = new ArrayList<String>();
		int m;
		for (Field field: clazz.getDeclaredFields()) {
			m = field.getModifiers();
			if (!Modifier.isStatic(m) 
					&& !Modifier.isNative(m)) {
				fields.add(field.getName());
			}
		}
		return fields;
	}
	
	// 2013-10-25
	public List getPublicStaticValues(Class class1) throws Exception {
		List values = new ArrayList();
		int m;
		Object value;
		for (Field field: c.getDeclaredFields()) {
			m = field.getModifiers();
			
			if (Modifier.isPublic(m) && Modifier.isStatic(m) 
					&& field.getType().equals(class1)) {
				value = field.get(class1);
				values.add(value);
			}
		}
		return values;
	}

	public List<String> toNameList(List<Field> fields) {
		List<String> fieldNames = new ArrayList<String>();
		for (Field field: fields) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}
	
	public String[] toNameArray(List<Field> fields) {
		String[] fieldNames = new String[fields.size()];
		for (int i=0; i<fields.size(); i++) {
			fieldNames[i] = fields.get(i).getName();
		}
		return fieldNames;
	}
	
	
	private boolean isNull(String s) {
		return s==null||s.equals("");
	}

	public List<Field> getTypedFields(Class clazz) {
		List<Field> fields = new ArrayList();
		Field field;
		for (String fieldName: normalFields) {
			field = getField(fieldName);
			if (field.getType().equals(clazz)) {
				fields.add(field);
			}
		}
		return fields;
	}

}
