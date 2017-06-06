package org.stan.yxgz.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stan.yxgz.pojo.Coach;
import org.stan.yxgz.pojo.CoachCourse;
import org.stan.yxgz.util.HttpCommonUtil;
import org.stan.yxgz.util.LRUCache;
import org.stan.yxgz.util.LRUCacheHashMap;
import org.stan.yxgz.util.PropertyUtils;
import org.stan.yxgz.util.StaticDataCache;
import org.stan.yxgz.util.UrlUtil;
import org.stan.yxgz.util.UrlUtil.HttpRequestData;


/**
 * 调用接口类，主要给前台jsp调用
 * @author chendan
 *
 */

public  class InterfaceService  {
	private static final long serialVersionUID = 1L;
	private static Logger log=LoggerFactory.getLogger(InterfaceService.class);
	private static String subjectSecond=PropertyUtils.getWebServiceProperty("subjectSecond");
    private static String subjectThird=PropertyUtils.getWebServiceProperty("subjectThrid");
    public static String[] getSubjectArray(){
    	return new String[]{subjectSecond,subjectThird};
    }
    public static Map<String,Object> isBandedNew(String username,String userno,String telphone,String openId) throws UnsupportedEncodingException{
    	//绑定	
		String url= PropertyUtils.getWebServiceProperty("login.newIsbind");
		url=url.replace("openId", openId);
		String opt="?";
		if(url.indexOf("?")>=0)
			opt="&";
		url=url+opt+"studentName="+URLEncoder.encode(URLEncoder.encode(username,"UTF-8"),"UTF-8")+"&studentCardId="+userno+"&studentPhone="+telphone;
		String json="";
		String app=PropertyUtils.getWebServiceProperty("app");
		try{
			if(app.equals("true")){
				HttpRequestData data = UrlUtil.sendGet(url);
				json=data.getResult();
			}else
				json="{\"success\":true,\"title\":\"操作成功\",\"reason\":\"操作成功\",\"result\":{studentId:0bd22f1f-2aee-4e74-8ee0-144ffedd3822}}";
		}catch(Exception e){
			e.printStackTrace();
			log.info("isBandedNew方法："+e.getMessage());
		}
		Map<String,Object> result=new HashMap<String, Object>();
		
		JSONObject obj = JSONObject.fromObject(json);
		boolean state =Boolean.valueOf(obj.get("success").toString());
		result.put("state", state);
		result.put("reason", obj.get("reason"));
		if(state){
			JSONObject reObj=JSONObject.fromObject(obj.get("result"));
			result.put("userId", reObj.get("studentId"));
		}
		return result;
    }
    public static Map<String,Object> getUserId(String openId){
    	Map<String,Object> result=new HashMap<String, Object>();
    	String url= PropertyUtils.getWebServiceProperty("login.user");
		url=url.replace("openId", openId);
		
		//log.info("根据openId判断此用户是否绑定："+url);
		String json="";
		String app=PropertyUtils.getWebServiceProperty("app");
		try{
			if(app.equals("true")){
				HttpRequestData httpData = UrlUtil.sendGet(url);
				json=httpData.getResult();
			}else
				json="{\"success\":true,\"title\":\"\",\"reason\":\"\",\"result\":\"{studentId:null,isbanded:true}\"}";
		}catch(Exception e){
			e.printStackTrace();
			log.info("getUserId方法："+e.getMessage());
		}
		System.out.println("-----判断用户是否已经登录---------"+json);
		JSONObject obj = JSONObject.fromObject(json);
		boolean state =Boolean.valueOf(obj.get("success").toString());
		result.put("state", state);
		result.put("reason", obj.get("reason"));
		if(state){
			JSONObject reObj=JSONObject.fromObject(obj.get("result"));
			result.put("isbanded", reObj.get("isbanded"));
			result.put("userId", reObj.get("studentId"));
		}
		return result;
    }
    
    
    //我的预约
    public static Map<String,Object> getMineCourse(String openId){
    	Map<String,Object> result=new HashMap<String, Object>();
    	String url= PropertyUtils.getWebServiceProperty("course.mine");
		url=url.replace("openId", openId);
		System.out.println("-------mine--"+url);
		
		String json="";
		String app=PropertyUtils.getWebServiceProperty("app");
		try{
			if(app.equals("true")){
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
			}else
				json="{\"success\":true,\"title\":null,\"reason\":null,\"result\":[{\"stCourseId\":\"4d9d6d2e-3993-4792-a22a-f1795e5f4bbb\",\"roasterJx\":{\"courseId\":\"e5163103-1914-4ce4-978e-0051d49f7e5d\",\"branchId\":\"27a7e11d-7329-410b-99f3-6333f24107a1\",\"branchName\":\"市内分点\",\"canSignNum\":2,\"common\":null,\"courseHour\":1,\"courseInNum\":3,\"courseInfo\":null,\"courseName\":\"广州程通驾校-市内分点-陈曦教练-科目二\",\"courseNoticBHour\":4,\"courseStat\":\"待上课\",\"courseStatCode\":\"DSK\",\"courseTimearea\":\"下午\",\"courseTimeareaCode\":\"XW\",\"createDate\":1430115569000,\"editDate\":1430115569000,\"editEndTime\":1430208000000,\"endTime\":1430211600000,\"isCourseNotic\":1,\"isRoastNotic\":1,\"jxId\":\"9f4d700e-54c4-4843-8ff0-3fcff6661682\",\"jxName\":\"广州程通驾校\",\"offlineNum\":0,\"roastNoticAHour\":24,\"signEndTime\":1430208000000,\"startTime\":null,\"subjectId\":\"de9581d9-d016-470b-9126-ae297b673e57\",\"subjectName\":\"科目二\",\"teacherId\":\"4a99e726-f6d2-48ac-9ed8-3ce35f438662\",\"teacherName\":\"陈曦\"},\"courseSt\":{\"stCourseId\":\"4d9d6d2e-3993-4792-a22a-f1795e5f4bbb\",\"bookTime\":1430116956000,\"cancelDate\":null,\"cancelInfo\":null,\"carScore\":null,\"common\":null,\"courseId\":\"e5163103-1914-4ce4-978e-0051d49f7e5d\",\"courseName\":\"广州程通驾校-市内分点-陈曦教练-科目二\",\"courseStat\":\"待上课\",\"courseStatCode\":\"DSK\",\"implCourseStat\":null,\"implCourseStatCode\":null,\"isCancelBook\":0,\"isNormalCancel\":1,\"isUncareUnimpl\":0,\"learnInfo\":null,\"overTime\":null,\"scoreInfo\":null,\"scoreTime\":null,\"serviceScore\":null,\"startTime\":null,\"studentId\":\"0bd22f1f-2aee-4e74-8ee0-144ffedd3822\",\"studentName\":\"陈丹\",\"teacherReplyInfo\":null,\"teacherReplyTime\":null,\"teacherScore\":null,\"unimplInfo\":null}},{\"stCourseId\":\"d3e31eed-57be-440b-bdaf-de497ffeab56\",\"roasterJx\":{\"courseId\":\"039bb48e-aac0-4188-8a33-222bb4349441\",\"branchId\":\"27a7e11d-7329-410b-99f3-6333f24107a1\",\"branchName\":\"市内分点\",\"canSignNum\":0,\"common\":null,\"courseHour\":1,\"courseInNum\":3,\"courseInfo\":null,\"courseName\":\"广州程通驾校-市内分点-陈曦教练-科目二\",\"courseNoticBHour\":4,\"courseStat\":\"待上课\",\"courseStatCode\":\"DSK\",\"courseTimearea\":\"上午\",\"courseTimeareaCode\":\"SW\",\"createDate\":1430115569000,\"editDate\":1430115569000,\"editEndTime\":1430190000000,\"endTime\":1430193600000,\"isCourseNotic\":1,\"isRoastNotic\":1,\"jxId\":\"9f4d700e-54c4-4843-8ff0-3fcff6661682\",\"jxName\":\"广州程通驾校\",\"offlineNum\":0,\"roastNoticAHour\":24,\"signEndTime\":1430190000000,\"startTime\":null,\"subjectId\":\"de9581d9-d016-470b-9126-ae297b673e57\",\"subjectName\":\"科目二\",\"teacherId\":\"4a99e726-f6d2-48ac-9ed8-3ce35f438662\",\"teacherName\":\"陈曦\"},\"courseSt\":{\"stCourseId\":\"d3e31eed-57be-440b-bdaf-de497ffeab56\",\"bookTime\":1430116367000,\"cancelDate\":null,\"cancelInfo\":null,\"carScore\":null,\"common\":null,\"courseId\":\"039bb48e-aac0-4188-8a33-222bb4349441\",\"courseName\":\"广州程通驾校-市内分点-陈曦教练-科目二\",\"courseStat\":\"待上课\",\"courseStatCode\":\"DSK\",\"implCourseStat\":null,\"implCourseStatCode\":null,\"isCancelBook\":0,\"isNormalCancel\":1,\"isUncareUnimpl\":0,\"learnInfo\":null,\"overTime\":null,\"scoreInfo\":null,\"scoreTime\":null,\"serviceScore\":null,\"startTime\":null,\"studentId\":\"0bd22f1f-2aee-4e74-8ee0-144ffedd3822\",\"studentName\":\"陈丹\",\"teacherReplyInfo\":null,\"teacherReplyTime\":null,\"teacherScore\":null,\"unimplInfo\":null}}]}";
		}catch(Exception e){
			e.printStackTrace();
			log.info("getMineCourse方法："+e.getMessage());
		}
		System.out.println(json);
		result = HttpCommonUtil.transMyCourseForJson(json);
		return result;
    }
    //是否绑定
	public static Map<String,Object> isLogin(String openId){
		Map<String,Object> result=new HashMap<String, Object>();
		//获取课程地址
		String url= PropertyUtils.getWebServiceProperty("login.isbind");
		url=url.replace("openId", openId);
		/*String opt="?";
		if(url.indexOf("?")>=0)
			opt="&";
		url=url;*/
		//log.info("根据openId判断此用户是否绑定："+url);
		String json="";
		String app=PropertyUtils.getWebServiceProperty("app");
		try{
			if(app.equals("true")){
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
		}else
			json="{\"success\":true,\"title\":\"操作成功\",\"reason\":\"操作成功\",\"result\":{\"openId\":\"0011223\",\"studentId\":\"22333222\",\"subjectId\":\"de9581d9-d016-470b-9126-ae297b673e57\",\"subjectName\":\"用户所考科目名称\",\"branchId\":\"网点编号\",\"branchName\":\"网点名称\",\"subjectTeachers\":{\"351742ee-ebcc-40cc-b5fc-fb3a39131e14\":[],\"de9581d9-d016-470b-9126-ae297b673e57\":[{\"teacherId\":\"教练编号\",\"teacherName\":\"教练名称\",\"jxId\":\"驾校编号\",\"jxName\":\"驾校名称\",\"branchId\":\"网点编号\",\"branchName\":\"网点名称\",\"subjectId\":\"教练所教科目编号\",\"subjectName\":\"教练所教科目名称\",\"tLogo\":\"教练头像地址\",\"scroe\":\"评分\",\"scroeNum\":\"点评数\",\"checkNum\":\"教练被浏览数\",\"likeNum\":\"教练被赞数\",\"duteAge\":\"教龄N月\",\"sex\":\"性别\",\"courseDays\":[{\"day\":\"课程日期\",\"subjectId\":\"科目编号\",\"subjectName\":\"科目名称\",\"totalNum\":\"当天可接纳总人数\",\"canBookNum\":\"现在剩余可以预约人数\",\"totalNumOfMorn\":\"上午可接纳总人数\",\"canBookNumOfMorn\":\"上午剩余可预约人数\",\"totalNumOfAftern\":\"下午可接纳总人数\",\"canBookNumOfAftern\":\"下午剩余可预约人数\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]},{\"day\":\"2015-04-15\",\"subjectId\":\"xxxxxx\",\"subjectName\":\"xxxxxxx\",\"totalNum\":\"24\",\"canBookNum\":\"16\",\"totalNumOfMorn\":\"9\",\"canBookNumOfMorn\":\"6\",\"totalNumOfAftern\":\"15\",\"canBookNumOfAftern\":\"10\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]}],\"hot\":false}]},\"banded\":false}}";
		}catch(Exception e){
			e.printStackTrace();
			log.info("isLogin方法："+e.getMessage());
		}
		log.info("-----预约 json-------"+json);
		Map<String,Object> dataMap = HttpCommonUtil.transForJson(json);
		boolean state=Boolean.valueOf(dataMap.get("state").toString());
		if(state){
			String banded=dataMap.get("banded").toString();
			if(banded.equals("true")){
				String subjectId = dataMap.get("subjectId").toString();
				String userId=dataMap.get("userId").toString();
				String branchName=dataMap.get("branchName").toString();
				String subjectName=dataMap.get("subjectName").toString();
				String branchId=dataMap.get("branchId").toString();
				
				String [] subjects=getSubjectArray();
				List<CoachCourse> coachListData=null;
				List<CoachCourse> coachListDatas=null;
				StaticDataCache cache = StaticDataCache.getInstance();
				for(String s:subjects){
					coachListDatas = (List<CoachCourse>)dataMap.get(s);
					if(s.equals(subjectId)){
						coachListData=coachListDatas;
					}
					if(!cache.isCachMapContain(userId+":"+subjectId))
						cache.putCache(userId+":"+s, coachListDatas);
				}
				result.put("userId", userId);
				result.put("branchId", branchId);
				result.put("branchName", branchName);
				result.put("data", coachListData);
				result.put("datas",coachListDatas);
				result.put("subjectId",subjectId);
				result.put("state", state);
				result.put("subjectName", subjectName);
			}
			result.put("banded", banded);
		}
		result.put("state", state);
		String reason=dataMap.get("reason")+"";
		result.put("reason", reason);
		
		return result;
	}
	public static List<CoachCourse> getCoachCourseById(String userId,String subjectId){
		LRUCache<String, Object> lruMap = StaticDataCache.getInstance().getCache();
		List<CoachCourse> coachList=(List<CoachCourse>)lruMap.get(userId+":"+subjectId);
		return coachList;
	}
	/**
	 * 根据教练Id去缓存里面查找，换成里面存的是整个科目的教练信息
	 * @param userId
	 * @param subjectId
	 * @param coachId
	 * @param openId
	 * @return
	 */
	public Coach getCoachById(String userId,String subjectId,String coachId,String openId){
		Coach coachs=null;
		LRUCacheHashMap lruMap = StaticDataCache.getInstance().getCachMap();
		List<CoachCourse> coachList=(List<CoachCourse>)lruMap.get(userId+":"+subjectId);
		for(CoachCourse coachDourse:coachList){
			Coach coach=coachDourse.getCoach();
			if(coach.getId().equals(coachId))
				coachs=coach;
			else
				continue;
		}
		return coachs;
	}
	//绑定
	public static  Map<String,Object> login(String openId,String username,String userno,String telphone){
		Map<String,Object> result=new HashMap<String, Object>();
		return result;
	}
	
	//获取教练课程表信息
	public static Map<String,Object> getSubjects(String openId,String coachId,String day){
		Map<String,Object> result=new HashMap<String, Object>();
		//获取课程地址
		String url= PropertyUtils.getWebServiceProperty("school.course");
		url=url.replace("teacherId", coachId).replace("bookingDate", day);
		/*String opt="?";
		if(url.indexOf("?")>=0)
			opt="&";
		url=url;*/
		//log.info("根据教练获取课程信息访问地址："+url);
		String json="";
		String app=PropertyUtils.getWebServiceProperty("app");
		try{
			if(app.equals("true")){
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
			}else
			json="{\"success\":true,\"title\":null,\"reason\":null,\"result\":{\"day\":\"2015-04-15\",\"teacherId\":\"11112\",\"teacherName\":\"张三\",\"subjectId\":\"de9581d9-d016-470b-9126-ae297b673e57\",\"subjectName\":\"用户所考科目名称\",\"totalNum\":\"24\",\"canBookNum\":\"12\",\"totalNumOfMorn\":\"9\",\"canBookNumOfMorn\":\"2\",\"totalNumOfAftern\":\"15\",\"canBookNumOfAftern\":\"10\",\"coursesOfMorn\":[{\"courseId\":\"001\",\"courseName\":\"课程名称001\",\"startTime\":\"8:00\",\"endTime\":\"9：00\",\"courseTimearea\":\"am\",\"courseInNum\":\"12\",\"canSignNum\":\"1\"},{\"courseId\":\"xxxxxxxxxxxx\",\"courseName\":\"xxxxxxxxxxx\",\"startTime\":\"09:00:00\",\"endTime\":\"10:00:00\",\"courseTimearea\":\"上午\",\"courseInNum\":\"3\",\"canSignNum\":\"1\"},{\"courseId\":\"xxxxxxx\",\"courseName\":\"xxxxxx\",\"startTime\":\"10:00:00\",\"endTime\":\"11:00:00\",\"courseTimearea\":\"上午\",\"courseInNum\":\"3\",\"canSignNum\":\"0\"},{\"courseId\":\"xxxxxxx\",\"courseName\":\"xxxxxx\",\"startTime\":\"11:00:00\",\"endTime\":\"12:00:00\",\"courseTimearea\":\"上午\",\"courseInNum\":\"3\",\"canSignNum\":\"1\"}],\"coursesOfAftern\":[{\"courseId\":\"xxxxxxx\",\"courseName\":\"xxxxxx\",\"startTime\":\"14:00:00\",\"endTime\":\"15:00:00\",\"courseTimearea\":\"下午\",\"courseInNum\":\"3\",\"canSignNum\":\"3\"},{\"courseId\":\"xxxxxxx\",\"courseName\":\"xxxxxx\",\"startTime\":\"15:00:00\",\"endTime\":\"16:00:00\",\"courseTimearea\":\"下午\",\"courseInNum\":\"3\",\"canSignNum\":\"2\"},{\"courseId\":\"xxxxxxx\",\"courseName\":\"xxxxxx\",\"startTime\":\"16:00:00\",\"endTime\":\"17:00:00\",\"courseTimearea\":\"下午\",\"courseInNum\":\"3\",\"canSignNum\":\"1\"},{\"courseId\":\"xxxxxxx\",\"courseName\":\"xxxxxx\",\"startTime\":\"17:00:00\",\"endTime\":\"18:00:00\",\"courseTimearea\":\"下午\",\"courseInNum\":\"3\",\"canSignNum\":\"0\"}]}}";
		}catch(Exception e){
			e.printStackTrace();
			log.info("getSubjects方法："+e.getMessage());
		}
		Map<String,Object> data = HttpCommonUtil.transCourseForJson(json);
		//boolean state=Boolean.valueOf(data.get("state").toString());
		//result.put("state", state);
		//String reason=data.get("reason").toString();
		//result.put("reason", reason);
		result=data;
		
		
		return result;
	}
	/**
	 * 我是教练是否已注册
	 * @param openId  根据用户openId来判断
	 * 返回数据result属性说明：
     *teacherId:如果绑定了，有教师编号值，否则为null;
     *isbanded:如果绑定了为true,否则为false;
     *checkstat:如果未绑定为null，否则为注册审核状态值(待审核、审核通过、审核未通过)
	 * @return
	 */
	public static Map<String,Object> isRegister(String openId,HttpServletRequest request){
		Map<String,Object> result=new HashMap<String, Object>();
		//我是教练是否已经注册
		String url= PropertyUtils.getWebServiceProperty("coach.mine");
		url=url.replace("openId", openId);
		String json="";
		try{
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
		}catch(Exception e){
			e.printStackTrace();
			log.info("isRegister方法："+e.getMessage());
		}
		Map<String,String> stateMap=new HashMap<String, String>();
		stateMap.put("DSH", "待审核");
		stateMap.put("SHTG", "审核通过");
		stateMap.put("SHWTG", "审核未通过");
		
		JSONObject obj = JSONObject.fromObject(json);  
	    boolean state=Boolean.valueOf(obj.getString("success"));
	    String title=obj.getString("title").toString();
	    String reason=obj.getString("reason");
		//{"success":true,"title":"","reason":"","result":"{teacherId:null,isbanded:false,checkstat:null}"}
		result.put("state", state);
		result.put("reason", reason);
		request.setAttribute("state", state);
		request.setAttribute("reason", reason);
		if(state){
			json = obj.get("result").toString();
			JSONObject obj2 = JSONObject.fromObject(json); 
			String teacherId=obj2.getString("teacherId");
			boolean isbanded=obj2.getBoolean("isbanded");
			String checkstat=obj2.getString("checkstat");
			//teacherId="sssssdddd";
			//isbanded=true;
			//checkstat="SHTG";
			result.put("isbanded", isbanded);
			result.put("teacherId", teacherId);
			request.setAttribute("isbanded", isbanded);
			request.setAttribute("teacherId", teacherId);
			if(StringUtils.isNotBlank(teacherId) || isbanded){  //已经绑定
				if(checkstat.equals("DSH") || checkstat.equals("SHWTG")){  //这两种状态不能看到学员信息，待审核，审核未通过
					String msg=stateMap.get(checkstat);
					result.put("checkstat", checkstat);
					result.put("msg", msg);
					request.setAttribute("data", "[{}]");
					request.setAttribute("datas","[{}]");
					request.setAttribute("text", "您的账号的审核状态为："+msg+".暂时无法查看当前信息");
					request.setAttribute("texts", "您的账号的审核状态为："+msg+".暂时无法查看当前信息");
					request.setAttribute("checkstat", checkstat);
					request.setAttribute("msg", msg);
				}else{//查看学员上课信息
				    String currentDate  = getCurrentData();
					String afterUrl= PropertyUtils.getWebServiceProperty("coach.after");
					afterUrl=afterUrl.replace("teacherId", teacherId);
					List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
					List<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
					List<String> text=new ArrayList<String>();
					data=InterfaceService.getCoachStudent(afterUrl,teacherId,text);
					String beforUrl= PropertyUtils.getWebServiceProperty("coach.before");
					List<String> texts=new ArrayList<String>();
					beforUrl=beforUrl.replace("teacherId", teacherId);
					datas=InterfaceService.getCoachStudentHistory(beforUrl,teacherId,texts,currentDate);
					
					request.setAttribute("historyDate", texts.get(1));
					/*request.setAttribute("data", "[{\"dataLine\":\"2015-05-14\",\"areaCode\":\"上午\",\"dataList\":[{\"dataTime\":\"8:00-9:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"9:00-10:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"10:00-11:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"11:00-12:00\",\"students\":\"张一,张二，张三，张思\"}]},{\"dataLine\":\"2015-05-14\",\"areaCode\":\"下午\",\"dataList\":[{\"dataTime\":\"13:00-14:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"14:00-15:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"15:00-16:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"16:00-17:00\",\"students\":\"张一,张二，张三，张思\"}]}]");
					request.setAttribute("datas","[{\"dataLine\":\"2015-05-14测试\",\"areaCode\":\"上午\",\"dataList\":[{\"dataTime\":\"8:00-9:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"9:00-10:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"10:00-11:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"11:00-12:00\",\"students\":\"张一,张二，张三，张思\"}]},{\"dataLine\":\"2015-05-14测试\",\"areaCode\":\"下午\",\"dataList\":[{\"dataTime\":\"13:00-14:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"14:00-15:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"15:00-16:00\",\"students\":\"张一,张二，张三，张思\"},{\"dataTime\":\"16:00-17:00\",\"students\":\"张一,张二，张三，张思\"}]}]");
					request.setAttribute("text", "陈教练:2015-05-14至2015-05-25一共15个课时，工休息5个");
					request.setAttribute("texts", "陈教练:2015-01-14至2015-05-13一共150个课时，工休息50个");*/
					
					request.setAttribute("data", data.size()<1?"[{}]":JSONArray.fromObject(data));
					request.setAttribute("datas",datas.size()<1?"[{}]":JSONArray.fromObject(datas));
					request.setAttribute("text", text.get(0));
					request.setAttribute("texts", texts.get(0));
				}
			}else{  //未绑定
				request.setAttribute("data", "[{}]");
				request.setAttribute("datas","[{}]");
				request.setAttribute("text", "");
				request.setAttribute("texts", "");
			}
		}else{
			request.setAttribute("data", "[{}]");
			request.setAttribute("datas","[{}]");
			request.setAttribute("text", "");
			request.setAttribute("texts", "");
		}
		return result;
	}
	
	public static Map<String,Object> getCommon(){
		Map<String,Object> result=new HashMap<String, Object>();
		String jxId=PropertyUtils.getWebServiceProperty("school.id");
		String url= PropertyUtils.getWebServiceProperty("coach.common");
		url=url.replace("jxId", jxId);
		String json="";
		try{
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
		}catch(Exception e){
			e.printStackTrace();
			log.info("getCommon方法："+e.getMessage());
		}
		JSONObject obj = JSONObject.fromObject(json);  
	    boolean state=Boolean.valueOf(obj.getString("success"));
	    String title=obj.getString("title").toString();
	    String reason=obj.getString("reason");
	    result.put("state", state);
	    result.put("reason",reason);
		if(state){
			json = obj.get("result").toString();
			JSONObject obj2 = JSONObject.fromObject(json); 
			String jxName=obj2.getString("jxName");
			JSONArray arryBranches=JSONArray.fromObject(obj2.get("branches"));
			List<Map<String,String>> listBranches=new ArrayList<Map<String,String>>();
			List<Map<String,String>> listTeaCarTypes=new ArrayList<Map<String,String>>();
			List<Map<String,String>> listCarTypes=new ArrayList<Map<String,String>>();
			for(int i=0;i<arryBranches.size();i++){
				Map<String,String> m=new HashMap<String, String>();
				JSONObject o = JSONObject.fromObject(arryBranches.get(i));
				m.put("id", o.getString("branchId"));
				m.put("name", o.getString("branchName"));
				m.put("branchAddress", o.getString("branchAddress"));
				listBranches.add(m);
			}
			JSONArray arryTeaCarTypes=obj2.getJSONArray("teaCarTypes");
			for(int i=0;i<arryTeaCarTypes.size();i++){
				Map<String,String> m=new HashMap<String, String>();
				JSONObject o = JSONObject.fromObject(arryTeaCarTypes.get(i));
				m.put("id", o.getString("dataId"));
				m.put("name", o.getString("dataName"));
				listTeaCarTypes.add(m);
			}
			
			JSONArray arryCarTypes=obj2.getJSONArray("carTypes");
			for(int i=0;i<arryCarTypes.size();i++){
				Map<String,String> m=new HashMap<String, String>();
				JSONObject o = JSONObject.fromObject(arryCarTypes.get(i));
				m.put("id", o.getString("dataId"));
				m.put("name", o.getString("dataName"));
				listCarTypes.add(m);
			}
			String [] sub=new String[]{subjectSecond,subjectThird};
			List<Map<String,String>> listSubs=new ArrayList<Map<String,String>>();
			for(int i=0;i<sub.length;i++){
				Map<String,String> subMap=new HashMap<String, String>();
				subMap.put("id", sub[i]);
				String name=i==0?"科目二":"科目三";
				subMap.put("name", name);
				listSubs.add(subMap);
			}
			
			result.put("jxName", jxName);
			result.put("jxId", jxId);
			result.put("subject", listSubs.toArray());
			result.put("carType", listCarTypes.toArray());
			result.put("branch", listBranches.toArray());
			result.put("coachCarType", listTeaCarTypes.toArray());
		}
		return result;
	}

	public static Map<String,Object> getCoachBeforStudent(String teacherId){
		Map<String,Object> map=new HashMap<String, Object>();
		String url= PropertyUtils.getWebServiceProperty("coach.before");
		url=url.replace("teacherId", teacherId);
		String json="";
		try{
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
		}catch(Exception e){
			e.printStackTrace();
			log.info("getCoachBeforStudent方法："+e.getMessage());
		}
		//{"success":true,"title":null,"reason":null,"result":{"teacherId":null,"teacherName":null,
		//"jxId":null,"jxName":null,"branchId":null,"branchName":null,"startDay":"2015-05-12 00:00:00","endDay":"2015-05-15 00:00:00","totolCoures":0,"noCoures":0,"duteCoures":0,"roasterDays":[]}}
		System.out.println(json);
		JSONObject obj = JSONObject.fromObject(json);  
		boolean state=obj.getBoolean("success");
		String reason=obj.getString("reason");
		if(state){
			JSONObject o=JSONObject.fromObject(obj.get("result"));
			String Id=o.getString("teacherId");
			String name=o.getString("teacherName");
			String jxId=o.getString("jxId");
			String jxName=o.getString("jxName");
			String branchId=o.getString("branchId");
			String branchName=o.getString("branchName");
			String startDay=o.getString("startDay");
			String endDay=o.getString("endDay");
			String totolCoures=o.getString("totolCoures");
			String duteCoures=o.getString("duteCoures");
			String noCoures=o.getString("noCoures");
			JSONArray arry=JSONArray.fromObject(o.get("roasterDays"));
			for(int a=0;a<arry.size();a++){
				
			}
		}
		return map;
	}
	
	public static List<Map<String,Object>> getCoachStudent(String url,String teacherId,List<String> message){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		String json="";
		try{
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
		}catch(Exception e){
			e.printStackTrace();
			log.info("getCoachStudent方法："+e.getMessage());
		}
		JSONObject obj = JSONObject.fromObject(json);  
		boolean state=obj.getBoolean("success");
		String reason=obj.getString("reason");
		if(state){
			JSONObject o=JSONObject.fromObject(obj.get("result"));
			String Id=o.getString("teacherId");
			String name=o.getString("teacherName");
			String jxId=o.getString("jxId");
			String jxName=o.getString("jxName");
			String branchId=o.getString("branchId");
			String branchName=o.getString("branchName");
			String startDay=o.getString("startDay");
			startDay=startDay.substring(0, 11);
			String endDay=o.getString("endDay");
			endDay=endDay.substring(0,11);
			String totolCoures=o.getString("totolCoures");
			String duteCoures=o.getString("duteCoures");
			String noCoures=o.getString("noCoures");
			String appendHtml="";
			if((StringUtils.isNotBlank(name) || StringUtils.isNotBlank(Id))&&!totolCoures.equals("0")){
				appendHtml=name+"教练:"+startDay+" 至 "+endDay+"(共)"+totolCoures+"课时. 休息"+noCoures+"课时";
			}else{
				appendHtml="您暂时没有上课记录!";
			}
			message.add(appendHtml);
			JSONArray arry=JSONArray.fromObject(o.get("roasterDays"));
			String currentDate=getCurrentData();
			for(int a=0;a<arry.size();a++){
				JSONObject o1=JSONObject.fromObject(arry.get(a));
				
				Map<String,Object> totalMap=new HashMap<String, Object>();
				String day=o1.getString("day");
				//if(currentDate.equals(day)){
					
					JSONArray morArry=JSONArray.fromObject(o1.get("coursesOfMorn"));
					List<Map<String,String>> list=new ArrayList<Map<String,String>>();
					for(int i=0;i<morArry.size();i++){  //每个课时
						totalMap.put("dataLine", day);
						Map<String,String> courseMap=new HashMap<String, String>();
						JSONObject cO1=JSONObject.fromObject(morArry.get(i));
						totalMap.put("areaCode", cO1.getString("courseTimearea"));
						String  startTime=cO1.getString("startTime").split(" ")[1];
						startTime=startTime.substring(0, 5);
						String endTime=cO1.getString("endTime").split(" ")[1];
						endTime=endTime.substring(0, 5);
						String areaCode=startTime+"-"+endTime;
						List<String> students=new ArrayList<String>();
						JSONArray studs=JSONArray.fromObject(cO1.get("courseSts"));
						String student=new String();
						for(int j=0;j<studs.size();j++){
							JSONObject oo=JSONObject.fromObject(studs.get(j));
							students.add(oo.getString("studentName"));
							student+=oo.getString("studentName")+",";
						}
						courseMap.put("dataTime", areaCode);
						courseMap.put("students", StringUtils.isNotBlank(student)?student.substring(0, student.length()-1):"无人");
						list.add(courseMap);
					}
					totalMap.put("dataList", list);
					result.add(totalMap);  //加载上午的课时
					JSONArray afterArry=JSONArray.fromObject(o1.get("coursesOfAftern"));
					totalMap=new HashMap<String, Object>();
					list=new ArrayList<Map<String,String>>();
					for(int i=0;i<afterArry.size();i++){
						totalMap.put("dataLine", day);
						Map<String,String> courseMap=new HashMap<String, String>();
						JSONObject cO1=JSONObject.fromObject(afterArry.get(i));
						totalMap.put("areaCode", cO1.getString("courseTimearea"));
						String  startTime=cO1.getString("startTime").split(" ")[1];
						startTime=startTime.substring(0, 5);
						String endTime=cO1.getString("endTime").split(" ")[1];
						endTime=endTime.substring(0, 5);
						String areaCode=startTime+"-"+endTime;
						List<String> students=new ArrayList<String>();
						JSONArray studs=JSONArray.fromObject(cO1.get("courseSts"));
						String student=new String();
						for(int j=0;j<studs.size();j++){
							JSONObject oo=JSONObject.fromObject(studs.get(j));
							students.add(oo.getString("studentName"));
							student+=oo.getString("studentName")+",";
						}
						courseMap.put("dataTime", areaCode);
						courseMap.put("students", StringUtils.isNotBlank(student)?student.substring(0, student.length()-1):"无人");
						list.add(courseMap);
					}
					totalMap.put("dataList", list);
					result.add(totalMap);  //加载下午的课时
					//break;
				//}else{
				//	continue;
				//}
			}
		}
		return result;
	}
	
	public static List<Map<String,Object>> getCoachStudentHistory(String url,String teacherId,List<String> message,String currentDate){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		url=url.replace("endDate", currentDate);
		String json="";
		try{
			HttpRequestData httpData = UrlUtil.sendGet(url);
			json=httpData.getResult();
		}catch(Exception e){
			e.printStackTrace();
			log.info("getCoachStudentHistory方法："+e.getMessage());
		}
		JSONObject obj = JSONObject.fromObject(json);  
		boolean state=obj.getBoolean("success");
		String reason=obj.getString("reason");
		if(state){
			JSONObject o=JSONObject.fromObject(obj.get("result"));
			String Id=o.getString("teacherId");
			String name=o.getString("teacherName");
			String jxId=o.getString("jxId");
			String jxName=o.getString("jxName");
			String branchId=o.getString("branchId");
			String branchName=o.getString("branchName");
			String startDay=o.getString("startDay");
			startDay=startDay.substring(0, 11);
			String endDay=o.getString("endDay");
			endDay=endDay.substring(0,11);
			String totolCoures=o.getString("totolCoures");
			String duteCoures=o.getString("duteCoures");
			String noCoures=o.getString("noCoures");
			String appendHtml="";
			if(StringUtils.isNotBlank(name) || StringUtils.isNotBlank(Id)){
				appendHtml=name+"教练:"+startDay+" 至 "+endDay+"(共)"+totolCoures+"课时. 休息"+noCoures+"课时";
			}else{
				appendHtml="您暂时没有上课记录!";
			}
			message.add(appendHtml);
			JSONArray arry=JSONArray.fromObject(o.get("roasterDays"));
			if(arry.size()<=0)
				message.add(currentDate);
			for(int a=0;a<arry.size();a++){
				JSONObject o1=JSONObject.fromObject(arry.get(a));
				
				Map<String,Object> totalMap=new HashMap<String, Object>();
				String day=o1.getString("day");
				if((a+1)==arry.size())
					message.add(day);
				//if(currentDate.equals(day)){
					
					JSONArray morArry=JSONArray.fromObject(o1.get("coursesOfMorn"));
					List<Map<String,String>> list=new ArrayList<Map<String,String>>();
					for(int i=0;i<morArry.size();i++){  //每个课时
						totalMap.put("dataLine", day);
						Map<String,String> courseMap=new HashMap<String, String>();
						JSONObject cO1=JSONObject.fromObject(morArry.get(i));
						totalMap.put("areaCode", cO1.getString("courseTimearea"));
						String  startTime=cO1.getString("startTime").split(" ")[1];
						startTime=startTime.substring(0, 5);
						String endTime=cO1.getString("endTime").split(" ")[1];
						endTime=endTime.substring(0, 5);
						String areaCode=startTime+"-"+endTime;
						List<String> students=new ArrayList<String>();
						JSONArray studs=JSONArray.fromObject(cO1.get("courseSts"));
						String student=new String();
						for(int j=0;j<studs.size();j++){
							JSONObject oo=JSONObject.fromObject(studs.get(j));
							students.add(oo.getString("studentName"));
							student+=oo.getString("studentName")+",";
						}
						courseMap.put("dataTime", areaCode);
						courseMap.put("students", StringUtils.isNotBlank(student)?student.substring(0, student.length()-1):"无人");
						list.add(courseMap);
					}
					totalMap.put("dataList", list);
					result.add(totalMap);  //加载上午的课时
					JSONArray afterArry=JSONArray.fromObject(o1.get("coursesOfAftern"));
					totalMap=new HashMap<String, Object>();
					list=new ArrayList<Map<String,String>>();
					for(int i=0;i<afterArry.size();i++){
						totalMap.put("dataLine", day);
						Map<String,String> courseMap=new HashMap<String, String>();
						JSONObject cO1=JSONObject.fromObject(afterArry.get(i));
						totalMap.put("areaCode", cO1.getString("courseTimearea"));
						String  startTime=cO1.getString("startTime").split(" ")[1];
						startTime=startTime.substring(0, 5);
						String endTime=cO1.getString("endTime").split(" ")[1];
						endTime=endTime.substring(0, 5);
						String areaCode=startTime+"-"+endTime;
						List<String> students=new ArrayList<String>();
						JSONArray studs=JSONArray.fromObject(cO1.get("courseSts"));
						String student=new String();
						for(int j=0;j<studs.size();j++){
							JSONObject oo=JSONObject.fromObject(studs.get(j));
							students.add(oo.getString("studentName"));
							student+=oo.getString("studentName")+",";
						}
						courseMap.put("dataTime", areaCode);
						courseMap.put("students", StringUtils.isNotBlank(student)?student.substring(0, student.length()-1):"无人");
						list.add(courseMap);
					}
					totalMap.put("dataList", list);
					result.add(totalMap);  //加载下午的课时
				//}else{
				//	continue;
				//}
			}
		}else{
			message.add(currentDate);
		}
		return result;
	}
	
	public static String getCurrentData(){
		 Date d = new Date();  
		 boolean falg=false;
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	     String currentDate = sdf.format(d);
	     return currentDate;
	}
	public static void unbinded(String openId){
		String url=PropertyUtils.getWebServiceProperty("coach.unbanded");
		url=url.replace("openId", openId);
		HttpRequestData httpData = UrlUtil.sendGet(url);
		String json=httpData.getResult();
		System.out.println("---------------------------------------------------"+json);
	}
	/**
	 * 机器人对话
	 * @param type  是否是关注,type=attention  关注，其他就是机器人对话
	 * @param value
	 * @return
	 */
	public static String getRobotStr(String type,String value){
		StringBuffer buffer=new StringBuffer();
		if(type.equals("attention")){
			
		}else{
			
		}
		return buffer.toString();
	}
}
