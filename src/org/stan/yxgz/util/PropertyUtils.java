package org.stan.yxgz.util;

import java.io.File;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 读取系统权限参数配置文件
 * @author zhangtq
 * @since 2012/07/31
 */
public class PropertyUtils {

	
	private static PropertiesConfiguration config;
	
    private static final String WEBSERVICE_PROPERTY = "webservice.properties";
    private static Properties jdbcProperties;
	
	private PropertyUtils(){
		
	}
	
	
	public static String getWebServiceProperty(String prop) {
    	String result = null;
        if (jdbcProperties == null) {
            try {
                jdbcProperties = new Properties();
                String wifile = "resource" + File.separator + WEBSERVICE_PROPERTY;
                jdbcProperties.load(PropertyUtils.class.getClassLoader().getResourceAsStream(wifile));
                result = jdbcProperties.getProperty(prop);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
        	result = jdbcProperties.getProperty(prop);
        }
        return result;
    }
	
	public static void main(String []args){
		String url = getWebServiceProperty("report.tree");
		System.out.println(url);
	}
}
