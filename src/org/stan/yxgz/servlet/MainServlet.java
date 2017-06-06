package org.stan.yxgz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stan.yxgz.pojo.CoachCourse;
import org.stan.yxgz.pojo.Course;
import org.stan.yxgz.service.InterfaceService;
import org.stan.yxgz.util.HttpCommonUtil;
import org.stan.yxgz.util.PropertyUtils;
import org.stan.yxgz.util.StaticDataCache;
import org.stan.yxgz.util.UrlUtil;
import org.stan.yxgz.util.UrlUtil.HttpRequestData;



public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 11111111L;
	private Logger log=LoggerFactory.getLogger(MainServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			response.setContentType("text/html");
		    response.setCharacterEncoding("utf-8");
		    request.setCharacterEncoding("UTF-8");
			String type=request.getParameter("type");
			
			String openId=request.getParameter("openId");
			
			if(type.equals("login")){
				String courseId=request.getParameter("courseId");
				StaticDataCache cach=StaticDataCache.getInstance();
				/*if(cach.isCachMapContain("data")){
					List<CoachCourse> day = (List<CoachCourse>)cach.getCache("data");
					List<CoachCourse> days = (List<CoachCourse>)cach.getCache("datas");
					request.setAttribute("data",JSONArray.fromObject(day).toString() );
	            	request.setAttribute("datas",JSONArray.fromObject(days).toString() );
	            	request.setAttribute("totals", days.size());
	            	request.setAttribute("total",day.size());
	            	request.getSession().setAttribute("userId", cach.getCache("userId"));
	            	request.getSession().setAttribute("banded", cach.getCache("banded"));
	            	request.setAttribute("banded", cach.getCache("banded"));
	            	request.setAttribute("state", "true");
		            request.setAttribute("openId", openId);
					request.getRequestDispatcher("/reportTree/main-002.jsp").forward(request, response);
				}else{*/
					Map<String,Object> result=new HashMap<String, Object>();
		            result = InterfaceService.isLogin(openId);
		            boolean falg=Boolean.valueOf(result.get("state").toString());
		            if(falg){
			            boolean state=Boolean.valueOf(result.get("banded").toString());
			            if(state){  //已经绑定
			            	List<CoachCourse> day=(List<CoachCourse>)result.get("data");
			            	List<CoachCourse> days=(List<CoachCourse>)result.get("datas");
			            	cach.putCache("data", day);
			            	cach.putCache("datas", days);
			            	cach.putCache("branchName", result.get("branchName"));
			            	cach.putCache("subjectId",result.get("subjectId"));
			            	String userId=result.get("userId").toString();
			            	cach.putCache("userId", userId);
			            	cach.putCache("banded", state);
			            	request.setAttribute("data",JSONArray.fromObject(day).toString() );
			            	request.setAttribute("datas",JSONArray.fromObject(days).toString() );
			            	request.setAttribute("totals", days.size());
			            	request.setAttribute("total",day.size());
			            	request.setAttribute("branchName",result.get("branchName"));
			            	request.setAttribute("subjectId",result.get("subjectId"));
			            	request.getSession().setAttribute("userId", userId);
			            	request.getSession().setAttribute("openId", openId);
			            	request.getSession().setAttribute("banded", state);
						}else{
							request.setAttribute("data","[{}]");
			            	request.setAttribute("datas","[{}]" );
						}
			            request.setAttribute("banded", state);
			        }else{
			        	request.setAttribute("data","[{}]");
		            	request.setAttribute("datas","[{}]" );
			        }
		            request.setAttribute("state", falg);
		            request.setAttribute("reason", result.get("reason"));
		            request.setAttribute("openId", openId);
		            request.getRequestDispatcher("/reportTree/main-002.jsp").forward(request, response);
				//}
			}else if(type.equals("get")){
				String day=request.getParameter("day");
				String coachId=request.getParameter("coachId");
				Map<String,Object> map=(Map<String,Object>)InterfaceService.getSubjects(openId, coachId, day);
				boolean state=Boolean.parseBoolean(map.get("state").toString());
				JSONObject jsonObject = new JSONObject();
				if(state){
					List<Course> amList=(List<Course>)map.get("amData");
					List<Course> pmList=(List<Course>)map.get("pmData");
					jsonObject.put("amData", JSONArray.fromObject(amList).toString());
					jsonObject.put("pmData", JSONArray.fromObject(pmList).toString());
					jsonObject.put("day",day);
					jsonObject.put("coachName", map.get("coachName"));
				}
				jsonObject.put("state", state);
				jsonObject.put("reason", map.get("reason"));
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
				
			}else if(type.equals("mine")){  //我的预约
				System.out.println("我的预约开始---------------------");
				JSONObject jsonObject = new JSONObject();
				Map<String,Object> map=InterfaceService.getMineCourse(openId);
				jsonObject.put("state", map.get("state"));
				jsonObject.put("dataS0A", map.get("dataS0A")==null?"[{}]":map.get("dataS0A"));
				jsonObject.put("dataS0X", map.get("dataS0X")==null?"[{}]":map.get("dataS0X"));
				//jsonObject.put("userId", map.get("userId"));
				//jsonObject.put("isbanded", map.get("isbanded"));
				jsonObject.put("reason",map.get("reason"));
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else if(type.equals("cancel")){  //取消课程
				JSONObject jsonObject = new JSONObject();
				String courseId=request.getParameter("courseId");
				String url= PropertyUtils.getWebServiceProperty("course.cancel");
				url=url.replace("openId", openId).replace("stCourseId", courseId);
				
				HttpRequestData httpData = UrlUtil.sendGet(url);
				String json=httpData.getResult();
				JSONObject obj=JSONObject.fromObject(json);
				Map<String,Object> map=InterfaceService.getMineCourse(openId);
				jsonObject.put("dataS0A", map.get("dataS0A")==null?"[{}]":map.get("dataS0A"));
				jsonObject.put("dataS0X", map.get("dataS0X")==null?"[{}]":map.get("dataS0X"));
				jsonObject.put("reason",obj.get("reason"));
				jsonObject.put("state", obj.get("success"));
				jsonObject.put("title", obj.get("title"));
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else if(type.equals("evalute")){   //教练评价
				
				JSONObject jsonObject = new JSONObject();
				String courseId=request.getParameter("courseId");
				String url= PropertyUtils.getWebServiceProperty("coach.evaluate");
				url=url.replace("stCourseId", courseId);
				String desc=request.getParameter("desc");  //scoreInfo

				String parameter1=request.getParameter("param1");  //teacherScore
				String parameter2=request.getParameter("param2");  //carScore
				String parameter3=request.getParameter("param3");  //serviceScore
				url+="?teacherScore="+parameter1+"&carScore="+parameter2+"&serviceScore="+parameter3+"&scoreInfo="+URLEncoder.encode(URLEncoder.encode(desc,"UTF-8"),"UTF-8");
				HttpRequestData httpData = UrlUtil.sendGet(url);
				String json=httpData.getResult();
				JSONObject obj=JSONObject.fromObject(json);
				System.out.println(json);
				jsonObject.put("reason",obj.get("reason"));
				jsonObject.put("state", obj.get("success"));
				jsonObject.put("title", obj.get("title"));
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}
			else{
				String msg="";
				JSONObject jsonObject = new JSONObject();
				String url=PropertyUtils.getWebServiceProperty("course.conform");
				String courseId=request.getParameter("courseId");
				url=url.replace("courseId", courseId).replace("openId", openId);	
				
				
				request.getSession().setAttribute("userId","12345");
				String app=PropertyUtils.getWebServiceProperty("app");
				String json="";
				if(app.equals("true")){
				HttpRequestData data = UrlUtil.sendGet(url);
				json=data.getResult();
				}else
				json="{\"success\":true,\"title\":\"操作异常\",\"reason\":\"该节课程已经没有空位\",\"result\":null}";
				JSONObject obj = JSONObject.fromObject(json);  
				boolean state=Boolean.valueOf(obj.getString("success"));
				String title=obj.getString("title").toString();
				String reason=obj.getString("reason");
				jsonObject.put("state", state);
				jsonObject.put("title", title);
				jsonObject.put("reason", reason);
				//StaticDataCache.getInstance().clear();
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
				
				//log.info(msg);
				}
		
		
	}
	private static String decode(String str) {
		try {
			if (StringUtils.isNotBlank(str)) {
				str = java.net.URLDecoder.decode(str, "UTF-8");
			} else {
				str = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

}
