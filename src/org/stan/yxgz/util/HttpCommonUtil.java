package org.stan.yxgz.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.stan.yxgz.pojo.Coach;
import org.stan.yxgz.pojo.CoachCourse;
import org.stan.yxgz.pojo.Course;
import org.stan.yxgz.pojo.CourseDay;
import org.stan.yxgz.pojo.UserCourse;


public class HttpCommonUtil {
	private static final String TAG = "HttpUtil";

	private static final String RE_COURSE="DSK";
	private static final String NOT_COURSE="WSK";
	private static final String COURSE_REEVAL="YSKDPJ";
	private static final String COURSE_EVAL="YPJ";
	private static final String COURSE_CANCEL="YQX";
	private static String imgUrl=PropertyUtils.getWebServiceProperty("img.url");
	private static  String time="";
	 public static boolean compare_date(String rowDate, String currentDate) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        boolean falg=true;
	        try {
	            Date dt1 = df.parse(rowDate);
	            Date dt2 = df.parse(currentDate);
	           // System.out.println("rowDate:"+dt1.getTime()+"------"+"currentDate:"+dt2.getTime());
		            if (dt1.getTime() <= dt2.getTime()) {
		                falg=false;
		            }
	           } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return falg;
	    }
	public static boolean getdate(String current,String areaDate){
		 Date d = new Date();  
		 boolean falg=false;
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	     String currentDate = sdf.format(d);
		 Calendar now = Calendar.getInstance();  
	     int currentArea=now.get(Calendar.HOUR_OF_DAY);
	     boolean result=compare_date(current,currentDate);
	     if(!result){
		     if(currentDate.equals(current)){
		    	 String [] date = areaDate.split("-");
		    	 int start=Integer.parseInt(date[0].split(":")[0].replaceAll(" ", ""));
		    	 int end=Integer.parseInt(date[1].split(":")[0].replaceAll(" ", ""));
		    	 if(start<=currentArea || currentArea>=end){
		    		 falg=true;
		    	 }
		     }else{
		    	 falg=true;
		     }
	     }
	     return falg;
	}
	public static String mUpdate(String url) {

		// System.out.println("--------------->" + url);
		URL mURL;
		String result = null;
		try {
			mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			conn.connect();
			InputStream inStrm = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm));
			result = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return "网络连接错误！";
		}
		return result;
	}

	public static String login(String url) {
		URL mURL;
		String result = null;
		try {
			mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			conn.connect();
			InputStream inStrm = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm));
			result = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return "2222";
		}
		return result;
	}

	public static String getMsg(String url) {
		URL mURL;
		String result = null;
		try {
			mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			conn.connect();
			InputStream inStrm = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm));
			result = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return "网络连接错误！";
		}
		return result;
	}

	

	public static String loadContactJson(String url) {
		// System.out.println("请求ＲＵＬ地址："+url);
		URL mURL;
		String result = null;
		try {
			mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			conn.connect();
			InputStream inStrm = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm,"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) { // 处理换行符
				sb.append(line);
			}
			inStrm.close();
			result = sb.toString();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "网络连接错误！";
		}
	}

	public static String loadReport(String url) {
		URL mURL;
		String result = null;
		try {
			mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			conn.connect();
			InputStream inStrm = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) { // 处理换行符
				sb.append(line);
			}
			inStrm.close();
			result = sb.toString();

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "网络连接错误！";
		}
	}

	public static InputStream getInputStreamFromUrl(String url) {
		InputStream is = null;
		try {
			URL mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			is = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
	public static Map<String,Object> transMyCourseForJson(String json){
		Map<String,Object> map=new HashMap<String, Object>();
		JSONObject obj = JSONObject.fromObject(json);  
	    boolean state=Boolean.valueOf(obj.getString("success"));
	    String title=obj.getString("title").toString();
	    String reason=obj.getString("reason");
		json = obj.get("result").toString();
		List<UserCourse> listS0A=new ArrayList<UserCourse>();
		List<UserCourse> listS0X=new ArrayList<UserCourse>();
		if(state){
			JSONArray arry=JSONArray.fromObject(json);
			for(int i=0;i<arry.size();i++){
				UserCourse user=new UserCourse();
				JSONObject object=JSONObject.fromObject(arry.getJSONObject(i));
				user.setUserCourseId(object.get("stCourseId").toString());
				JSONArray coachArry=JSONArray.fromObject(object.get("roasterJx"));
				Map<String,String> coachMap=new HashMap<String, String>();
				for(int a=0;a<coachArry.size();a++){
					JSONObject o=JSONObject.fromObject(coachArry.get(a));
					coachMap.put("courseId", o.getString("courseId"));
					coachMap.put("branchId", o.getString("branchId"));
					coachMap.put("branchName", o.getString("branchName"));
					coachMap.put("courseStat", o.getString("courseStat"));
					coachMap.put("courseStatCode", o.getString("courseStatCode"));
					coachMap.put("courseTimearea", o.getString("courseTimearea"));
					coachMap.put("createDate", o.getString("createDate"));
					coachMap.put("editDate", o.getString("editDate"));
					coachMap.put("enditEndTime", o.getString("editEndTime"));
					coachMap.put("common", imgUrl+o.getString("common"));
					coachMap.put("courseInfo", o.getString("courseInfo"));
					coachMap.put("subjectId", o.getString("subjectId"));
					coachMap.put("subjectName", o.getString("subjectName"));
					coachMap.put("teacherName", o.getString("teacherName"));
					coachMap.put("teacherId", o.getString("teacherId"));
					coachMap.put("courseName", o.getString("courseName"));
					String timeS=o.getString("startTime");
					String timeE=o.getString("endTime");
					coachMap.put("startTime", timeS);
					coachMap.put("endTime", timeE);
					String date=timeS.substring(0,timeS.indexOf(" "));
					String hourS=timeS.substring(timeS.indexOf(" "),timeS.length()-3);
					String hourE=timeE.substring(timeE.indexOf(" "),timeE.length()-3);
					coachMap.put("time", date);
					coachMap.put("hourArear", hourS+"-"+hourE);
					
				}
				user.setTime(coachMap.get("time"));
				user.setHourArear(coachMap.get("hourArear"));
				user.setCourseName(coachMap.get("courseName"));
				user.setCoachId(coachMap.get("courseId"));
				user.setBranchId(coachMap.get("branchId"));
				user.setBranchName(coachMap.get("branchName"));
				user.setCommon(coachMap.get("common"));
				user.setCourseInfo(coachMap.get("courseInfo"));
				user.setCourseState(coachMap.get("courseStat"));
				user.setCourseStateCode(coachMap.get("courseStatCode"));
				user.setCreateDate(coachMap.get("createDate"));
				user.setCourseTimearea(coachMap.get("courseTimearea"));
				user.setEndDate(coachMap.get("endTime"));
				user.setEditDate(coachMap.get("editEndTime"));
				user.setEditEndTime(coachMap.get("enditEndTime"));
				user.setSubjectId(coachMap.get("subjectId"));
				user.setSubjectName(coachMap.get("subjectName"));
				user.setCoachId(coachMap.get("teacherId"));
				user.setCoachName(coachMap.get("coachName"));
				
				JSONArray userArry=JSONArray.fromObject(object.get("courseSt"));
				Map<String,String> userMap=new HashMap<String, String>();
				for(int a=0;a<userArry.size();a++){
					JSONObject o=JSONObject.fromObject(userArry.get(a));
					userMap.put("studentId", o.getString("studentId"));
					userMap.put("studentName", o.getString("studentName"));
				}
				user.setStartTime(coachMap.get("startTime"));
				user.setUserId(userMap.get("studentId"));
				user.setUserName(userMap.get("studentName"));
				boolean falg=getdate(user.getTime(),user.getHourArear());
				if(user.getCourseStateCode().equals(RE_COURSE) && !falg)
					listS0A.add(user);
				else
					listS0X.add(user);
			}
		}
		map.put("dataS0A", listS0A.size()<=0?"":listS0A);
		map.put("dataS0X", listS0X.size()<=0?"":listS0X);
		map.put("state", state);
		map.put("reason", reason);
		return map;
	}
	
	public static Map<String,Object> transCourseForJson(String json){
		Map<String,Object> map=new HashMap<String, Object>();
	    JSONObject obj = JSONObject.fromObject(json);  
	    boolean state=Boolean.valueOf(obj.getString("success"));
	    String title=obj.getString("title").toString();
	    String reason=obj.getString("reason");
		json = obj.get("result").toString();
		if(state){
			JSONObject obj2 = JSONObject.fromObject(json); 
			CourseDay days=new CourseDay();
			String day=obj2.getString("day").toString();
			String totalNum = obj2.getString("totalNum");
			String canBookNum=obj2.getString("canBookNum");
			String subjectId=obj2.getString("subjectId");
			String subjectName=obj2.getString("subjectName");
			String totalNumOfMorn =obj2.getString("totalNumOfMorn");
			String canBookNumOfMorn=obj2.getString("canBookNumOfMorn");
			String totalNumOfAftern =obj2.getString("totalNumOfAftern");
			String canBookNumOfAftern=obj2.getString("canBookNumOfAftern");
			map.put("day", day);
			map.put("coachId", obj2.get("teacherId"));
			map.put("coachName", obj2.get("teacherName"));
			map.put("totalNum", totalNum);
			map.put("canBookNum", canBookNum);
			map.put("subjectId", subjectId);
			map.put("subjectName", subjectName);
			map.put("totalNumOfMorn", totalNumOfMorn);
			map.put("canBookNumOfMorn", canBookNumOfMorn);
			map.put("totalNumOfAftern", totalNumOfAftern);
			map.put("canBookNumOfAftern", canBookNumOfAftern);
			days.setDay(day);
			days.setTotalNum(totalNum);
			days.setCanBookNum(canBookNum);
			days.setSubjectId(subjectId);
			days.setSubjectName(subjectName);
			days.setTotalNumOfam(canBookNumOfMorn);
			days.setCanBookNumOfam(totalNumOfMorn);
			days.setTotalNumOfpm(totalNumOfAftern);
			days.setCanBookNumOfpm(canBookNumOfAftern);
			JSONArray subdateam = obj2.getJSONArray("coursesOfMorn");
			List<Course> courseListAm=new ArrayList<Course>();
			int count1 = subdateam.size();  
			for(int i=0;i<count1;i++){
				Course course=new Course();
				JSONObject subObj=JSONObject.fromObject(subdateam.get(i));
				course.setCourseId(subObj.getString("courseId"));
				course.setCanSingNum(subObj.getString("courseName"));
				course.setStartTime(subObj.getString("startTime"));
				course.setEndTime(subObj.getString("endTime"));
				course.setCourseTimearea(subObj.getString("courseTimearea"));
				course.setCourseInNum(subObj.getString("courseInNum"));
				course.setCanSingNum(subObj.getString("canSignNum"));
				courseListAm.add(course);
			}
			JSONArray subdatepm=obj2.getJSONArray("coursesOfAftern");
			int count2 = subdatepm.size(); 
			List<Course> courseListPm=new ArrayList<Course>();
			for(int i=0;i<count2;i++){
				Course course=new Course();
				JSONObject subObj=JSONObject.fromObject(subdatepm.get(i));
				course.setCourseId(subObj.getString("courseId"));
				course.setCanSingNum(subObj.getString("courseName"));
				course.setStartTime(subObj.getString("startTime"));
				course.setEndTime(subObj.getString("endTime"));
				course.setCourseTimearea(subObj.getString("courseTimearea"));
				course.setCourseInNum(subObj.getString("courseInNum"));
				course.setCanSingNum(subObj.getString("canSignNum"));
				courseListPm.add(course);
			}
			map.put("amData", courseListAm);
			map.put("pmData", courseListPm);
		}
		map.put("state", state);
		map.put("reason", reason);
		
		return map;
	}
	
	public static Map<String,Object> transForJson(String json){
		Map<String,Object> map=new HashMap<String, Object>();
	    JSONObject obj = JSONObject.fromObject(json);  
	    boolean state=Boolean.valueOf(obj.getString("success"));
	    String title=obj.getString("title").toString();
	    String reason=obj.getString("reason");
	    String subjectSecond=PropertyUtils.getWebServiceProperty("subjectSecond");
	    String subjectThird=PropertyUtils.getWebServiceProperty("subjectThrid");
		json = obj.get("result").toString();
		if(state){
			JSONObject obj2 = JSONObject.fromObject(json); 
			String banded=obj2.getString("banded").toString();
			if(banded.equals("true")){
				String openId = obj2.getString("openId");
				String userId=obj2.getString("studentId");
				String subjectId=obj2.getString("subjectId");
				String subjectName=obj2.getString("subjectName");
				String branchId =obj2.getString("branchId");
				String branchName=obj2.getString("branchName");
				map.put("openId", openId);
				map.put("banded", banded);
				map.put("userId", userId);
				map.put("subjectId", subjectId);
				map.put("subjectName", subjectName);
				map.put("branchId", branchId);
				map.put("branchName", branchName);
				JSONObject subJson=JSONObject.fromObject(obj2.get("subjectTeachers"));
				//JSONArray dataJson= JSONArray.fromObject(obj2.get("subjectTeachers"));
				JSONArray subdate1 = subJson.getJSONArray(subjectSecond);
				int count1 = subdate1.size();  //科目2的教练
				List<CoachCourse> coachListSecond=new ArrayList<CoachCourse>();
				for(int i=0;i<count1;i++){
					CoachCourse coachCourse=new CoachCourse();
					Coach coach=new Coach();
					JSONObject subObj=JSONObject.fromObject(subdate1.get(i));
					coach.setId(subObj.getString("teacherId"));
					coach.setName(subObj.getString("teacherName"));
					coach.setJxId(subObj.getString("jxId"));
					coach.setJxName(subObj.getString("jxName"));
					coach.setBranchId(subObj.getString("branchId"));
					coach.setBranchName(subObj.getString("branchName"));
		            coach.setPicUrl(imgUrl+subObj.getString("tLogo"));
					coach.setScore(subObj.getString("scroe"));
					coach.setScoreNum(subObj.getString("scroeNum"));
					coach.setDes(subObj.getString("common"));
					coach.setCheckNum(subObj.getString("checkNum"));
					coach.setDuteAge(subObj.getString("duteAge"));
					coach.setSex(subObj.getString("sex"));
					coach.setIsHost(subObj.getString("hot"));
					JSONArray jsonCourses=subObj.getJSONArray("courseDays");
					
					List<CourseDay> dayList=new ArrayList<CourseDay>();
					for(int j=0;j<jsonCourses.size();j++){
						CourseDay day=new CourseDay();
						JSONObject jcourse=JSONObject.fromObject(jsonCourses.get(j));
						day.setDay(jcourse.getString("day"));
						day.setSubjectId(jcourse.getString("subjectId"));
						day.setSubjectName(jcourse.getString("subjectName"));
		                day.setTotalNum(jcourse.getString("totalNum"));
		                day.setCanBookNum(jcourse.getString("canBookNum"));
		                day.setTotalNum(jcourse.getString("totalNum"));
		                day.setCanBookNumOfam(jcourse.getString("canBookNumOfMorn"));
		                day.setTotalNumOfam(jcourse.getString("totalNumOfMorn"));
		                day.setTotalNumOfpm(jcourse.getString("totalNumOfAftern"));
		                day.setCanBookNumOfpm(jcourse.getString("canBookNumOfAftern"));
		                dayList.add(day);
					}
					coachCourse.setCoach(coach);
					coachCourse.setCourseDays(dayList);
					coachListSecond.add(coachCourse);
				}
				map.put(subjectSecond, coachListSecond);
				
				List<CoachCourse> coachListThird=new ArrayList<CoachCourse>();
				JSONArray subdate2 = subJson.getJSONArray(subjectThird);
				int count2=subdate2.size();   //科目3的教练
				for(int i=0;i<count2;i++){
					CoachCourse coachCourse=new CoachCourse();
					Coach coach=new Coach();
					JSONObject subObj=JSONObject.fromObject(subdate2.get(i));
					coach.setId(subObj.getString("teacherId"));
					coach.setName(subObj.getString("teacherName"));
					coach.setJxId(subObj.getString("jxId"));
					coach.setJxName(subObj.getString("jxName"));
					coach.setBranchId(subObj.getString("branchId"));
					coach.setBranchName(subObj.getString("branchName"));
		            coach.setPicUrl(imgUrl+subObj.getString("tLogo"));
					coach.setDes(subObj.getString("common"));
					coach.setScore(subObj.getString("scroe"));
					coach.setScoreNum(subObj.getString("scroeNum"));
					coach.setCheckNum(subObj.getString("checkNum"));
					coach.setDuteAge(subObj.getString("duteAge"));
					coach.setSex(subObj.getString("sex"));
					coach.setIsHost(subObj.getString("hot"));
					JSONArray jsonCourses=subObj.getJSONArray("courseDays");
					
					List<CourseDay> dayList=new ArrayList<CourseDay>();
					for(int j=0;j<jsonCourses.size();j++){
						CourseDay day=new CourseDay();
						JSONObject jcourse=JSONObject.fromObject(jsonCourses.get(j));
						day.setDay(jcourse.getString("day"));
						day.setSubjectId(jcourse.getString("subjectId"));
						day.setSubjectName(jcourse.getString("subjectName"));
		                day.setTotalNum(jcourse.getString("totalNum"));
		                day.setCanBookNum(jcourse.getString("canBookNum"));
		                day.setTotalNum(jcourse.getString("totalNum"));
		                day.setCanBookNumOfam(jcourse.getString("canBookNumOfMorn"));
		                day.setTotalNumOfam(jcourse.getString("totalNumOfMorn"));
		                day.setTotalNumOfpm(jcourse.getString("totalNumOfAftern"));
		                day.setCanBookNumOfpm(jcourse.getString("canBookNumOfAftern"));
		                dayList.add(day);
					}
					coachCourse.setCoach(coach);
					coachCourse.setCourseDays(dayList);
					coachListThird.add(coachCourse);
				}
				map.put(subjectThird, coachListThird);
			}else
				map.put("banded", banded);
			map.put("state", state);
		}else{
			map.put("state", state);
			map.put("reason", reason);
		}
		return map;
	}
	
	
	
	
	public static void main(String []strs){
		
		
	}

	
}