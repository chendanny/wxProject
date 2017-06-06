package org.stan.yxgz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import org.stan.yxgz.pojo.Coachs;
import org.stan.yxgz.pojo.CourseDay;
import org.stan.yxgz.service.InterfaceService;
import org.stan.yxgz.util.HttpCommonUtil;
import org.stan.yxgz.util.PropertyUtils;
import org.stan.yxgz.util.StaticDataCache;
import org.stan.yxgz.util.UrlUtil;
import org.stan.yxgz.util.UrlUtil.HttpRequestData;


public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 11111111L;
	private Logger log=LoggerFactory.getLogger(UserServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			response.setContentType("text/html");
		    response.setCharacterEncoding("utf-8");
		    request.setCharacterEncoding("UTF-8");
			String openId=request.getParameter("openId");
			String type=request.getParameter("type");
			//String username=new String(request.getParameter("username").getBytes("ISO-8859-1"),"UTF-8");  //用户姓名
			String username=request.getParameter("username");  //用户姓名
			String userno=request.getParameter("userno");      //用户卡号
			String telphone=request.getParameter("telphone");  //注册手机号码
			
			
			if(type.equals("login")){
				//绑定	
				String url= PropertyUtils.getWebServiceProperty("login.bind");
				url=url.replace("openId", openId);
				String opt="?";
				if(url.indexOf("?")>=0)
					opt="&";
				url=url+opt+"studentName="+URLEncoder.encode(URLEncoder.encode(username,"UTF-8"),"UTF-8")+"&studentCardId="+userno+"&studentPhone="+telphone;
				String json="";
				String app=PropertyUtils.getWebServiceProperty("app");
				if(app.equals("true")){
				HttpRequestData data = UrlUtil.sendGet(url);
				json=data.getResult();
				}else
				json="{\"success\":true,\"title\":\"操作成功\",\"reason\":\"操作成功\",\"result\":{\"openId\":\"0011223\",\"studentId\":\"22333222\",\"subjectId\":\"de9581d9-d016-470b-9126-ae297b673e57\",\"subjectName\":\"用户所考科目名称\",\"branchId\":\"网点编号\",\"branchName\":\"网点名称\",\"subjectTeachers\":{\"351742ee-ebcc-40cc-b5fc-fb3a39131e14\":[{\"teacherId\":\"教练编号\",\"teacherName\":\"教练名称\",\"jxId\":\"驾校编号\",\"jxName\":\"驾校名称\",\"branchId\":\"网点编号\",\"branchName\":\"网点名称\",\"subjectId\":\"教练所教科目编号\",\"subjectName\":\"教练所教科目名称\",\"tLogo\":\"教练头像地址\",\"scroe\":\"评分\",\"scroeNum\":\"点评数\",\"checkNum\":\"教练被浏览数\",\"likeNum\":\"教练被赞数\",\"duteAge\":\"教龄N月\",\"sex\":\"性别\",\"courseDays\":[{\"day\":\"课程日期\",\"subjectId\":\"科目编号\",\"subjectName\":\"科目名称\",\"totalNum\":\"当天可接纳总人数\",\"canBookNum\":\"现在剩余可以预约人数\",\"totalNumOfMorn\":\"上午可接纳总人数\",\"canBookNumOfMorn\":\"上午剩余可预约人数\",\"totalNumOfAftern\":\"下午可接纳总人数\",\"canBookNumOfAftern\":\"下午剩余可预约人数\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]},{\"day\":\"2015-04-15\",\"subjectId\":\"xxxxxx\",\"subjectName\":\"xxxxxxx\",\"totalNum\":\"24\",\"canBookNum\":\"16\",\"totalNumOfMorn\":\"9\",\"canBookNumOfMorn\":\"6\",\"totalNumOfAftern\":\"15\",\"canBookNumOfAftern\":\"10\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]}],\"hot\":false},{\"teacherId\":\"教练编号\",\"teacherName\":\"教练名称\",\"jxId\":\"驾校编号\",\"jxName\":\"驾校名称\",\"branchId\":\"网点编号\",\"branchName\":\"网点名称\",\"subjectId\":\"教练所教科目编号\",\"subjectName\":\"教练所教科目名称\",\"tLogo\":\"教练头像地址\",\"scroe\":\"评分\",\"scroeNum\":\"点评数\",\"checkNum\":\"教练被浏览数\",\"likeNum\":\"教练被赞数\",\"duteAge\":\"教龄N月\",\"sex\":\"性别\",\"courseDays\":[{\"day\":\"课程日期\",\"subjectId\":\"科目编号\",\"subjectName\":\"科目名称\",\"totalNum\":\"当天可接纳总人数\",\"canBookNum\":\"现在剩余可以预约人数\",\"totalNumOfMorn\":\"上午可接纳总人数\",\"canBookNumOfMorn\":\"上午剩余可预约人数\",\"totalNumOfAftern\":\"下午可接纳总人数\",\"canBookNumOfAftern\":\"下午剩余可预约人数\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]},{\"day\":\"2015-04-15\",\"subjectId\":\"xxxxxx\",\"subjectName\":\"xxxxxxx\",\"totalNum\":\"24\",\"canBookNum\":\"16\",\"totalNumOfMorn\":\"9\",\"canBookNumOfMorn\":\"6\",\"totalNumOfAftern\":\"15\",\"canBookNumOfAftern\":\"10\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]}],\"hot\":false}],\"de9581d9-d016-470b-9126-ae297b673e57\":[{\"teacherId\":\"教练编号\",\"teacherName\":\"教练名称\",\"jxId\":\"驾校编号\",\"jxName\":\"驾校名称\",\"branchId\":\"网点编号\",\"branchName\":\"网点名称\",\"subjectId\":\"教练所教科目编号\",\"subjectName\":\"教练所教科目名称\",\"tLogo\":\"教练头像地址\",\"scroe\":\"评分\",\"scroeNum\":\"点评数\",\"checkNum\":\"教练被浏览数\",\"likeNum\":\"教练被赞数\",\"duteAge\":\"教龄N月\",\"sex\":\"性别\",\"courseDays\":[{\"day\":\"课程日期\",\"subjectId\":\"科目编号\",\"subjectName\":\"科目名称\",\"totalNum\":\"当天可接纳总人数\",\"canBookNum\":\"现在剩余可以预约人数\",\"totalNumOfMorn\":\"上午可接纳总人数\",\"canBookNumOfMorn\":\"上午剩余可预约人数\",\"totalNumOfAftern\":\"下午可接纳总人数\",\"canBookNumOfAftern\":\"下午剩余可预约人数\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]},{\"day\":\"2015-04-15\",\"subjectId\":\"xxxxxx\",\"subjectName\":\"xxxxxxx\",\"totalNum\":\"24\",\"canBookNum\":\"16\",\"totalNumOfMorn\":\"9\",\"canBookNumOfMorn\":\"6\",\"totalNumOfAftern\":\"15\",\"canBookNumOfAftern\":\"10\",\"coursesOfMorn\":[],\"coursesOfAftern\":[]}],\"hot\":false}]},\"banded\":true}}";
				Map<String,Object> result=new HashMap<String, Object>();
				
				log.info("-----login json-------"+json);
				Map<String,Object> dataMap = HttpCommonUtil.transForJson(json);
				boolean state=Boolean.valueOf(dataMap.get("state").toString());
				String reason=dataMap.get("reason")+"";
				result.put("reason", reason);
				result.put("state", state);
				JSONObject jsonObject = new JSONObject();
				
				if(state){
	            	String subjectId = dataMap.get("subjectId").toString();
					String userId=dataMap.get("userId").toString();
					String branchName=dataMap.get("branchName").toString();
					String subjectName=dataMap.get("subjectName").toString();
					String branchId=dataMap.get("branchId").toString();
					String banded=dataMap.get("banded").toString();
					String [] subjects=InterfaceService.getSubjectArray();
					List<CoachCourse> coachListData=null;
					List<CoachCourse> coachListDatas=null;
					StaticDataCache cache = StaticDataCache.getInstance();
					for(String s:subjects){
						coachListDatas = (List<CoachCourse>)dataMap.get(s);
						if(s.equals(subjectId)){
							coachListData=coachListDatas;
						}
						if(!cache.isCachMapContain(userId+":"+subjectId)){
							cache.putCache(userId+":"+s, coachListDatas);
							
						}				
					}
					jsonObject.put("banded", banded);
					jsonObject.put("data",JSONArray.fromObject(coachListData).toString() );
					jsonObject.put("datas",JSONArray.fromObject(coachListDatas).toString() );
					jsonObject.put("totals", coachListDatas.size());
					jsonObject.put("total",coachListData.size());
					jsonObject.put("branchName",branchName);
					jsonObject.put("subjectId",subjectId);
					jsonObject.put("userId", userId);
					jsonObject.put("openId", openId);
					
				
					
					request.setAttribute("banded", banded);
	            	request.setAttribute("data",JSONArray.fromObject(coachListData).toString() );
	            	request.setAttribute("datas",JSONArray.fromObject(coachListDatas).toString() );
	            	request.setAttribute("totals", coachListDatas.size());
	            	request.setAttribute("total",coachListData.size());
	            	request.setAttribute("branchName",branchName);
	            	request.setAttribute("subjectId",subjectId);
	            	request.getSession().setAttribute("userId", userId);
	            	request.getSession().setAttribute("openId", openId);
				}else{
					request.setAttribute("data","[{}]");
	            	request.setAttribute("datas","[{}]" );
	            	request.setAttribute("banded", "");
	            	jsonObject.put("data","[{}]");
					jsonObject.put("datas","[{}]" );
					jsonObject.put("banded", "");
				}
				jsonObject.put("openId", openId);
				jsonObject.put("state", state);
				jsonObject.put("reason", reason);
				request.setAttribute("openId", openId);
				request.setAttribute("state", state);
				request.setAttribute("reason", reason);
	           // request.getRequestDispatcher("/reportTree/main-002.jsp").forward(request, response);
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else if(type.equals("mine")){
				Map<String,Object> result = InterfaceService.isBandedNew(username,userno,telphone,openId);
				boolean state=Boolean.valueOf(result.get("state").toString());
				String userId=String.valueOf(result.get("userId"));
				JSONObject jsonObject = new JSONObject();
				if(state && StringUtils.isNotBlank(userId)){
					Map<String,Object> map=InterfaceService.getMineCourse(openId);
					jsonObject.put("state", map.get("state"));
					jsonObject.put("dataS0A", map.get("dataS0A")==null?"[{}]":map.get("dataS0A"));
					jsonObject.put("dataS0X", map.get("dataS0X")==null?"[{}]":map.get("dataS0X"));
					jsonObject.put("userId", map.get("userId"));
					//jsonObject.put("isbanded", map.get("isbanded"));
					jsonObject.put("reason",map.get("reason"));
					PrintWriter out = response.getWriter();
					out.print(jsonObject);
					out.flush();
					out.close();
				}else{
					jsonObject.put("reason",result.get("reason"));
					jsonObject.put("state", state);
				}
				
				PrintWriter out = response.getWriter();
				out.print(jsonObject);
				out.flush();
				out.close();
			}else{
				/*if(state){
					String subjectId = dataMap.get("subjectId").toString();
					String userId=dataMap.get("userId").toString();
					String branchName=dataMap.get("branchName").toString();
					String subjectName=dataMap.get("subjectName").toString();
					String branchId=dataMap.get("branchId").toString();
					String banded=dataMap.get("banded").toString();
					String [] subjects=InterfaceService.getSubjectArray();
					List<CoachCourse> coachListData=null;
					List<CoachCourse> coachListDatas=null;
					StaticDataCache cache = StaticDataCache.getInstance();
					for(String s:subjects){
						coachListDatas = (List<CoachCourse>)dataMap.get(s);
						if(s.equals(subjectId)){
							coachListData=coachListDatas;
						}
						if(!cache.isCachMapContain(userId+":"+subjectId)){
							cache.putCache(userId+":"+s, coachListDatas);
							
						}				
					}
					result.put("userId", userId);
					result.put("branchId", branchId);
					result.put("branchName", branchName);
					//result.put("data", coachListData);
					result.put("data", coachListData);
					result.put("subjectId",subjectId);
					result.put("state", state);
					result.put("subjectName", subjectName);
					result.put("banded", banded);
					request.getSession().setAttribute("userId", userId);
					request.setAttribute("banded", banded);
					request.setAttribute("subjectId", subjectId);
					request.getSession().setAttribute("data", result);
					request.setAttribute("openId", openId);
				}
				request.getRequestDispatcher("/reportTree/main-001.jsp").forward(request, response);*/
			}
            
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
	/**
	 * 登录成功通过code显示区域列表
	 * @param code
	 * @param openId
	 * @return
	 */
	public String getTreeRegion(String code,String openId){
		String treeUrl= PropertyUtils.getWebServiceProperty("report.tree");
		
		treeUrl+="?code="+code;
		log.info("区域列表:"+treeUrl);
		//String json = HttpUtil.loadContactJson(treeUrl);
		String json="";
		
		//String result = HttpUtil.transForm(json);
		//result=result.replaceAll("array", "list");
		//System.out.println(result);
		String result="[{\"isparent\":false,\"id\":\"001\",\"name\":\"天河区\",\"type\":\"olap\",\"url\":\" subject\",\"list\":[]},{\"isparent\":false,\"id\":\"002\",\"name\":\"海珠区\",\"type\":\"olap\",\"url\":\"http://192.168.1.206:8081/rpt/jatoolsreport?as=dhtml&file=/reports/templates/2014042175720.xml&myfields=\",\"list\":[]}]";
		return result;
		
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
