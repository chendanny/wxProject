package org.stan.yxgz.test;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.stan.yxgz.util.JsonDateValueProcessor;

import com.dt.dtpt.mybatis.model.sijiao.EduStudent;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class InterfaceTest {

    public static final String ADD_URL = "http://120.76.152.25:8889/services/sijiao/addStudentByWx/123456/oUDNMwBwAOoiYp0JrqHOx6BFiupo";

    public static String params = "{\"addUser\":\"\",\"address\":\"广州天河\",\"area\":\"\",\"birthday\":\"2010-10-10\",\"city\":\"\",\"common\":\"\",\"editDate\":null,\"highestEdu\":0,\"inGrade\":0,\"inSchool\":\"\",\"inScore\":\"\",\"isInschool\":1,\"phone\":\"18027778787\",\"province\":\"\",\"qq\":0,\"sex\":0,\"studentId\":\"\",\"studentName\":\"charty\",\"userId\":\"\",\"wxOpenid\":\"oUDNMwBwAOoiYp0JrqHOx6BFiupo\",\"zipCode\":\"\"}";
    public static void appadd() {

        try {
            //创建连接
            URL url = new URL(ADD_URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());

            out.writeBytes(params);
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void test(){
    	try {
    		
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost method = new HttpPost(ADD_URL);
			StringEntity entity = new StringEntity(params,"utf-8");//解决中文乱码问题    
			entity.setContentEncoding("UTF-8");    
			entity.setContentType("application/json");    
			method.setEntity(entity);
			HttpResponse result = httpClient.execute(method);  
			  
            // 请求结束，返回结果  
            String resData = EntityUtils.toString(result.getEntity());  
            System.out.println(resData);
		} catch (Exception e) {
			e.printStackTrace();
		}    
    }
    
    public static void setData(){
    	JsonConfig jsonConfig = new JsonConfig();
    	jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
    	EduStudent es = new EduStudent();
    	es.setAddress("广州市天河区");
    	es.setStudentName("笑姜");
    	es.setPhone("13512726239");
    	es.setBirthday(new Date());
    	es.setSex(1);
    	es.setWxOpenid("oUDNMwBwAOoiYp0JrqHOx6BFiupo");
    	es.setHighestEdu(0);
    	es.setInGrade(1);
    	es.setInSchool("1");
    	JSONObject jo = JSONObject.fromObject(es,jsonConfig);
    	params = jo.toString();
    }
    
    public static void main(String[] args) {
    	setData();
        //appadd();
    	test();
    }

}
