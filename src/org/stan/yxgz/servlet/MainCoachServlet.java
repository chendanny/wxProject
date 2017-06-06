package org.stan.yxgz.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
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




public class MainCoachServlet extends HttpServlet {
	private static final long serialVersionUID = 11111111L;
	private Logger log=LoggerFactory.getLogger(MainCoachServlet.class);
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
			if(type.equals("isLogin")){  //判断是否已经注册过，   state=register  已经注册，未登录，state=login标识以及注册已经登录
				Map<String,Object> result=new HashMap<String,Object>();
				result = InterfaceService.isRegister(openId,request);
				boolean state=Boolean.valueOf(result.get("state").toString());
				request.setAttribute("openId", openId);
				/*request.setAttribute("state", result.get("state"));
				request.setAttribute("reason", result.get("reason"));*/
				
		        request.getRequestDispatcher("/reportTree/main-003.jsp").forward(request, response);
			}else if(type.equals("more")){
				String currentDate=request.getParameter("date");
				currentDate=StringUtils.isBlank(currentDate)?InterfaceService.getCurrentData():currentDate;
				String teacherId=request.getParameter("teacherId");
				List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
				String beforUrl= PropertyUtils.getWebServiceProperty("coach.before");
				List<String> text=new ArrayList<String>();
				beforUrl=beforUrl.replace("teacherId", teacherId);
				data=InterfaceService.getCoachStudentHistory(beforUrl,teacherId,text,currentDate);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("size", data.size());
				jsonObject.put("data", data);
				jsonObject.put("historyDate", text.get(1));
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else if(type.equals("common")){
				
				Map<String,Object> result=new HashMap<String,Object>();
				result = InterfaceService.getCommon();
				
				JSONObject jsonObject = new JSONObject();
				/*jsonObject.put("carType", "[{\"id\":1,\"name\":\"c1/c2\"},{\"id\":2,\"name\":\"c2/c2\"},{\"id\":3,\"name\":\"c2/c3\"},{\"id\":4,\"name\":\"c3/c4\"}]");
				jsonObject.put("coachCarType", "[{\"id\":1,\"name\":\"别克\"},{\"id\":2,\"name\":\"捷达\"},{\"id\":3,\"name\":\"大众A系列\"},{\"id\":4,\"name\":\"大众H系列\"}]");
				jsonObject.put("channel", "[{\"id\":1,\"name\":\"客服经理\"},{\"id\":2,\"name\":\"渠道经理\"},{\"id\":3,\"name\":\"其他1\"},{\"id\":4,\"name\":\"其他2\"}]");
				jsonObject.put("branch", "[{\"id\":1,\"name\":\"天河科韵路\"},{\"id\":2,\"name\":\"岑村\"},{\"id\":3,\"name\":\"小新塘\"},{\"id\":4,\"name\":\"天河客运站\"}]");
				*/
				jsonObject.put("state", result.get("state"));
				jsonObject.put("reason", result.get("reason"));
				jsonObject.put("subject",result.get("subject"));
				jsonObject.put("carType", result.get("carType"));
				jsonObject.put("coachCarType", result.get("coachCarType"));
				jsonObject.put("jxName",result.get("jxName"));
				jsonObject.put("jxId",result.get("jxId"));
				jsonObject.put("branch", result.get("branch"));
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else if(type.equals("resgister")){

				String base64=request.getParameter("base64");
				String branchAddress=request.getParameter("branchAddress");
				branchAddress=URLEncoder.encode(branchAddress,"UTF-8");
				String carNo=request.getParameter("carNo");
				carNo=URLEncoder.encode(carNo,"UTF-8");
				String carPno=request.getParameter("carPno");
				carPno=URLEncoder.encode(carPno,"UTF-8");
				String sex=request.getParameter("sex");
				String subjectId=request.getParameter("subjectId");
				String subjectName=request.getParameter("subjectName");
				subjectName=URLEncoder.encode(subjectName,"UTF-8");
				String telphone=request.getParameter("telphone");
				String username=request.getParameter("username");
				username=URLEncoder.encode(username,"UTF-8");
				String identityCard=request.getParameter("identityCard");
			    String password=request.getParameter("password");
			    String coachNo=request.getParameter("coachNo");
			    String coachDriverYear=request.getParameter("coachDriverYear");
			    String coachTeachYear=request.getParameter("coachTeachYear");
			    String studentInd=request.getParameter("studentInd");
			    String address=request.getParameter("address");
			    address=URLEncoder.encode(address,"UTF-8");
			    String branchId=request.getParameter("branchId");
			    String branchName=request.getParameter("branchName");
			    System.out.println(branchName);
			    branchName=URLEncoder.encode(branchName,"UTF-8");
			    System.out.println(branchName);
			    String carTypeId=request.getParameter("carTypeId");
			    String carTypeName=request.getParameter("carTypeName");
			    carTypeName=URLEncoder.encode(carTypeName,"UTF-8");
			    String coachCarTypeId=request.getParameter("coachCarTypeId");
			    String coachCarTypeName=request.getParameter("coachCarTypeName");
			    coachCarTypeName=URLEncoder.encode(coachCarTypeName,"UTF-8");
			    String jxId=request.getParameter("jxId");
			    String jxName=request.getParameter("jxName");
			    jxName=URLEncoder.encode(jxName,"UTF-8");
				String url= PropertyUtils.getWebServiceProperty("coach.register");
				String param="jxId="+jxId+"&jxName="+jxName+"&branchId="+branchId+"&branchName="+branchName+"&subjectId="+subjectId+"&subjectName="+subjectName+"&teacherName="+username+"&sex="+sex+"&duteAge="+coachTeachYear+"&cardId="+identityCard+"&phoneNo="+telphone+"&address="+address+"&carLicenseNo="+carNo+"&carLicenseAge="+coachDriverYear+"&password="+password+"&teaCarTypeCode="+coachCarTypeId+"&teaCarType="+coachCarTypeName+"&carTypeCode="+carTypeId+"&carType="+carTypeName+"&carNo="+carPno+"&placeAddress="+branchAddress+"&duteDate=2015-05-15"+"&duteLevelNo="+coachNo+"&logostr="+base64;
				url=url.replace("openId", openId);
				//url+="?"+param;
				System.out.println(url);
				//HttpRequestData httpData = UrlUtil.sendGet(url);//(url,"",param);
				HttpRequestData httpData=UrlUtil.sendPost1(url, "", param, 50000);
				String json=httpData.getResult();
				System.out.println(json);
				JSONObject obj=JSONObject.fromObject(json);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("state", obj.get("success"));
				jsonObject.put("reason", obj.get("reason"));
				jsonObject.put("data", "[{}]");
				jsonObject.put("datas", "[{}]");
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else if(type.equals("login")){
				String username=request.getParameter("username");
				String telphone=request.getParameter("telphone");
				String password=request.getParameter("password");
				System.out.println(username);
				System.out.println(URLEncoder.encode(URLEncoder.encode(username,"UTF-8"),"UTF-8"));
				String url= PropertyUtils.getWebServiceProperty("coach.login");
				url=url.replace("openId", openId);
				String param="teacherName="+URLEncoder.encode(URLEncoder.encode(username,"UTF-8"),"UTF-8")+"&password="+password+"&teacherPhone="+telphone;
				url+="?"+param;
				System.out.println(url);
				HttpRequestData httpData=UrlUtil.sendGet(url);
				String json=httpData.getResult();
				JSONObject jsonObject = new JSONObject();
				JSONObject obj=JSONObject.fromObject(json);
				boolean state =Boolean.valueOf(obj.get("success").toString());
				String reason=obj.get("reason")+"";
				if(state){
					obj=JSONObject.fromObject(obj.get("result"));
					boolean isbanded=obj.getBoolean("isbanded");
					String checkstat=obj.getString("checkstat");
					if(isbanded && checkstat.equals("SHTG")){
						String afterUrl= PropertyUtils.getWebServiceProperty("coach.after");
						afterUrl=afterUrl.replace("teacherId", obj.getString("teacherId"));
						List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
						List<String> text=new ArrayList<String>();
						data=InterfaceService.getCoachStudent(afterUrl,obj.getString("teacherId").toString(),text);
						String beforUrl= PropertyUtils.getWebServiceProperty("coach.before");
						List<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
						List<String> texts=new ArrayList<String>();
						beforUrl=beforUrl.replace("teacherId", obj.getString("teacherId"));
						String currentDate=InterfaceService.getCurrentData();
						datas=InterfaceService.getCoachStudentHistory(beforUrl,obj.getString("teacherId").toString(),texts,currentDate);
						jsonObject.put("data", JSONArray.fromObject(data));
						jsonObject.put("datas",JSONArray.fromObject(datas));
						jsonObject.put("text", text.get(0));
						jsonObject.put("texts", text.get(0));
					}else{
						jsonObject.put("data", "[{}]");
						jsonObject.put("datas","[{}]");
						jsonObject.put("text", "");
						jsonObject.put("texts", "");
					}
					jsonObject.put("isbanded",isbanded);
					jsonObject.put("checkstat",checkstat);
				}
				jsonObject.put("state", state);
				jsonObject.put("reason", reason);
				System.out.println(json);
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
				
			}else if(type.equals("unband")){
				String url=PropertyUtils.getWebServiceProperty("coach.unbanded");
				url.replace("openId", openId);
				HttpRequestData httpData = UrlUtil.sendGet(url);
				String json=httpData.getResult();
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
