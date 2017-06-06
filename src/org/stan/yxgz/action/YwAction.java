package org.stan.yxgz.action;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.stan.yxgz.pojo.PublicWx;
import org.stan.yxgz.pojo.User;
import org.stan.yxgz.pojo.util.PublicWxUtil;
import org.stan.yxgz.service.CommonService;
import org.stan.yxgz.service.WXinterfaceService;
import org.stan.yxgz.util.KanqActionSupport;
import org.stan.yxgz.util.PropertyUtils;
import org.stan.yxgz.util.UrlUtil;
import org.stan.yxgz.util.UrlUtil.HttpRequestData;
import org.stan.yxgz.util.YiXinUtil;
import org.stan.yxgz.zhifu.util.GetWxOrderno;
import org.stan.yxgz.zhifu.util.RequestHandler;
import org.stan.yxgz.zhifu.util.Sha1Util;
import org.stan.yxgz.zhifu.util.TenpayUtil;




import com.dt.dtpt.mybatis.model.sijiao.EduCourse;
import com.dt.dtpt.mybatis.model.sijiao.EduStudent;
import com.dt.dtpt.service.publicwx.PublicwxService;
import com.dt.dtpt.service.sijiao.SijiaoService;
/*
*<P>维权信息查询</P>
*
*蔡老师公众号模块。
*
*@author  chend
*/

@Controller
@RequestMapping("index")
public class YwAction extends KanqActionSupport {

	private User user=new User();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	

	private SijiaoService sijiao;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@RequestMapping(value = "mineClass", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView mineClass(@RequestParam("shId") String shId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/mine-process");
		if(PublicWxUtil.getPublicWx()==null){
			PublicWx wx=WXinterfaceService.getShOperation(shId);
			request.getSession().setAttribute("publicwx", wx);
		}
		view.addObject("shId", shId);
		return view;
	}
	
	@RequestMapping(value = "tempCourse", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView tempCourse(@RequestParam("shId") String shId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/temp-course");
		if(PublicWxUtil.getPublicWx()==null){
			PublicWx wx=WXinterfaceService.getShOperation(shId);
			request.getSession().setAttribute("publicwx", wx);
		}
		view.addObject("shId", shId);
		return view;
	}
	@RequestMapping(value = "tempMinCourse", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView tempMinCourse(@RequestParam("shId") String shId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/temp-minecourse");
		if(PublicWxUtil.getPublicWx()==null){
			PublicWx wx=WXinterfaceService.getShOperation(shId);
			request.getSession().setAttribute("publicwx", wx);
		}
		request.setAttribute("shId",shId);
		view.addObject("shId", shId);
		return view;
	}
	@RequestMapping(value = "tempProcess", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView tempProcess(@RequestParam("shId") String shId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/temp-process");
		if(PublicWxUtil.getPublicWx()==null){
			PublicWx wx=WXinterfaceService.getShOperation(shId);
			request.getSession().setAttribute("publicwx", wx);
		}
		view.addObject("shId", shId);
		return view;
	}
	
	//SijiaoService sijiao1;
	@RequestMapping(value = "courceList", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView courceList(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		//String shId=request.getParameter("shId");
		//String openId=request.getParameter("openId");
		String pageSize=request.getParameter("pageSize");
		String pageNuber=request.getParameter("pageNuber");
		pageSize=StringUtils.isBlank(pageSize)?"20":pageSize;
		pageNuber=StringUtils.isBlank(pageNuber)?"1":pageNuber;
		String results=CommonService.isWxManerger(shId, openId);
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/courceList");
		
		
		Map<String, Object> result = WXinterfaceService.findWxCourses(null, shId, pageNuber, pageSize);
		view.addObject("dataList", result);
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("isAdmin", results);
		return view;
	}
	
	//添加课程
	@RequestMapping(value = "addCource")
	@ResponseBody
	public Object addCource(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		Map<String,Object> map =new HashMap<String, Object>();
		//String shId=request.getParameter("shId");
		//String openId=request.getParameter("openId");
		
		String courceName=request.getParameter("courceName");
		String courceType=request.getParameter("courceType");
		String teacherName=request.getParameter("teacherName");
		String btime=request.getParameter("btime");
		String etime=request.getParameter("etime");
		String ptime=request.getParameter("ptime");
		String period=request.getParameter("period");
		String money=request.getParameter("money");
		String paddress=request.getParameter("paddress");
		String pcount=request.getParameter("pcount");
		String ptotal=request.getParameter("ptotal");
		
		String common=request.getParameter("common");
		
		EduCourse course=new EduCourse();
		course.setCourseName(courceName);
		course.setTeacherName(teacherName);
		course.setCommon(common);
		course.setSubject(courceType);
		course.setMaxStudents(Integer.parseInt(ptotal));
		//course.setYear(period);
		course.setCourseInfo(common);
		course.setSemester(Integer.parseInt(period));
		course.setSubjectSub(courceType);
		course.setStartDate(sdf.parse(btime));
		course.setEndDate(sdf.parse(etime));
		course.setTeachTime(ptime);
		course.setTeachAddress(paddress);
		course.setTotalCourse(Integer.parseInt(pcount));
		course.setCourseJe(BigDecimal.valueOf(Double.parseDouble(money)));
		Map<String, Object> resultC = WXinterfaceService.addCourseByWx(course, openId, shId);
		System.out.println("添加课程"+resultC);
		System.out.println("添加课程状态："+resultC.get("state").toString());
		Object state = resultC.get("state");
		
		map.put("state", state);
		map.put("shId", shId);
		map.put("openId", openId);
		return JSONObject.fromObject(map);
	}
	
	
	@RequestMapping(value = "preAddCource", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preAddCource(@RequestParam("shId") String shId,@RequestParam("openId") String openId,@RequestParam("typeId") String typeId) throws Exception {
		ModelAndView view=new ModelAndView();
		//String shId=request.getParameter("shId");
		//String openId=request.getParameter("openId");
		if(typeId.equals("1")){
			view.setViewName("/WEB-INF/page/index/courceAdd");
		}else{
			view.setViewName("/WEB-INF/page/index/cource"+typeId+"Add");
		}
		view.addObject("typeId", typeId);
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		return view;
	}
	
	
	
	@RequestMapping(value = "register", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView register(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		//String openId=request.getParameter("openId");
		//String shId=request.getParameter("shId");
		String courseId=request.getParameter("courseId");
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/register");
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("courseId", courseId);
		return view;
	}
	@RequestMapping(value = "isLogin" )
	@ResponseBody
	public Object isLogin(@RequestParam("shId") String shId,@RequestParam("openId") String openId){
		//String openId=request.getParameter("openId");
		Map map=WXinterfaceService.findStudentByOpenId(openId);
		return JSONObject.fromObject(map);
	}
	@RequestMapping(value = "subUser" )
	@ResponseBody
	public Object subUser(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		Map<String,Object> map =new HashMap<String, Object>();
		//String shId=request.getParameter("shId");
		String studentName=request.getParameter("studentName");
		shId=StringUtils.isBlank(shId)?PropertyUtils.getWebServiceProperty("shId"):shId;
		String phone=request.getParameter("phone");
		String sex=request.getParameter("sex");
		String address=request.getParameter("address");
		String bornYear=request.getParameter("bornYear");
		String inSchool=request.getParameter("inSchool");
		Date bronDate=sdf.parse(bornYear);
		EduStudent stu=new EduStudent();
		stu.setAddress(address);
		stu.setSex(Integer.parseInt(sex));
		stu.setBirthday(bronDate);
		//stu.setCommon(common);
		stu.setIsInschool(Integer.parseInt(inSchool));
		stu.setStudentName(studentName);
		stu.setPhone(phone);
		//stu.setUserId(openId);
		stu.setWxOpenid(openId);
		Map<String,Object> result = WXinterfaceService.addStudentByWx(stu,openId,shId);
		Object state=result.get("state");
		map.put("userId", result.get("userId"));
		map.put("state", state);
		map.put("openId", openId);
		return JSONObject.fromObject(map);	
		//return view;
	}
	
	
	@RequestMapping(value = "courseBuy" )
	@ResponseBody
	public ModelAndView courseBuy(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		ModelAndView view=new ModelAndView("/zhifu1/buy");
		//String shId=request.getParameter("shId");
		//String openId=request.getParameter("openId");
		String courseId=request.getParameter("courseId");
		String courseSId=request.getParameter("courseSid");
		Map map=WXinterfaceService.getCourse(courseId);
		if(StringUtils.isBlank(courseSId)){
			Map cmap=WXinterfaceService.addCourseByWx(openId, courseId);
			courseSId=cmap.get("courseSid")+"";
		}
		Map course=(Map)map.get("data");
		//调用微信支付功能  统一订单
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("courseId",courseId);
		view.addObject("courseSid",courseSId);
		view.addAllObjects(map);
		return view;
	}
	
	@RequestMapping(value = "addMineCourse" )
	@ResponseBody
	public Object addMineCourse(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		//String openId=request.getParameter("openId");
		String courseId=request.getParameter("courseId");
		Map cmap=WXinterfaceService.addCourseByWx(openId, courseId);
		//return cmap.get("courseSid");
		Map m=new HashMap<String, String>();
		m.put("data", cmap.get("courseSid"));
		return JSONObject.fromObject(m);
	}
	
	@RequestMapping(value = "courseZhiFu" )
	@ResponseBody
	public ModelAndView courseZhiFu(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		ModelAndView view=new ModelAndView("/zhifu1/buy");
		//String shId=request.getParameter("shId");
		//String openId=request.getParameter("openId");
		String courseId=request.getParameter("courseId");
		Map map=WXinterfaceService.getCourse(courseId);
		Map cmap=WXinterfaceService.addCourseByWx(openId, courseId);
		Map course=(Map)map.get("data");
		
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("courseId",courseId);
		view.addObject("courseSid",cmap.get("courseSid"));
		view.addAllObjects(map);
		return view;
	}	
	
	@RequestMapping(value = "isZhifuOut" )
	@ResponseBody
	public Object isZhifuOut() throws ParseException{
		String courseSid=request.getParameter("courseSid");
		String orderId=request.getParameter("orderId");
		Map map=WXinterfaceService.isZhifuOut(courseSid,orderId);
		return map;
	}
	@RequestMapping(value = "message" )
	@ResponseBody
	public Object message(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException, UnsupportedEncodingException{
		//String openId=request.getParameter("openId");
		String courseName=request.getParameter("courseName");
		String courseSid=request.getParameter("courseSid");
		courseName=java.net.URLDecoder.decode(courseName, "UTF-8");
		String jine=request.getParameter("payJe");
		YiXinUtil.templateMessage(shId,openId, courseName, jine,courseSid);
		//是否发送管理员
		WXinterfaceService.sendMessageToManager(shId,openId, courseName, jine,courseSid);
		
		return "true";
	}
	
	@RequestMapping(value = "payOk" )
	@ResponseBody
	public Object payOk() throws ParseException{
		//String openId=request.getParameter("openId");
		String courseSid=request.getParameter("courseSid");
		String payJe=request.getParameter("payJe");
		Map map=WXinterfaceService.payOk(courseSid,payJe);
		return map;
	}
	//user.min.course.list
	@RequestMapping(value = "minCourstList" )
	@ResponseBody
	public ModelAndView minCourstList(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		if(PublicWxUtil.getPublicWx()==null){
			PublicWx wx=WXinterfaceService.getShOperation(shId);
			request.getSession().setAttribute("publicwx", wx);
		}
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/mine-courseList");
		Map map=WXinterfaceService.findMineCourses(shId,openId);
		
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("dataList", map);
		return view;
	}
	
	@RequestMapping(value = "tempMyCenter", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView tempMyCenter(@RequestParam("shId") String shId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/temp-center");
		view.addObject("shId", shId);
		return view;
	}
	//user.min.my.center
		@RequestMapping(value = "myCenter" )
		@ResponseBody
		public ModelAndView myCenter(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
			ModelAndView view=new ModelAndView("/WEB-INF/page/index/my-center");
			String results=CommonService.isWxManerger(shId, openId);
			view.addObject("shId", shId);
			view.addObject("openId", openId);
			view.addObject("isAdmin", results);
			return view;
		}
	
	
	@RequestMapping(value = "minDetailCourse" )
	@ResponseBody
	public ModelAndView minDetailCourse(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/courseDetail");
		String courseSid=request.getParameter("courseSid");
		Map map=WXinterfaceService.findStId(shId,courseSid);
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("dataList", map);
		return view;
	}
	
	@RequestMapping(value = "minCourstLCList" )
	@ResponseBody
	public ModelAndView minCourstLCList(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException{
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/mine-process");
		Map map=WXinterfaceService.findMineCoursesProcess(shId,openId);
		
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("dataList", map);
		return view;
	}
	
	
	@RequestMapping(value = "isExist", method = RequestMethod.GET )
	@ResponseBody
	public String isExist(){
		String rspData = "false";
	
		return rspData;
	}
	
	@RequestMapping(value="isBuy")
	@ResponseBody
	public Object isBuy(@RequestParam("shId") String shId,@RequestParam("openId") String openId){
		String app = PropertyUtils.getWebServiceProperty("isTest");
		String courseId=request.getParameter("courseId");
		String state="false";
		if(app.equals("true"))
			state="false";
		else
			state= WXinterfaceService.isBuy(shId, openId, courseId);
		
		return state;
	}
	
	@RequestMapping(value = "orderList", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView orderList(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/orderList");
		String pageSize=request.getParameter("pageSize");
		String pageNuber=request.getParameter("pageNuber");
		pageSize=StringUtils.isBlank(pageSize)?"20":pageSize;
		pageNuber=StringUtils.isBlank(pageNuber)?"1":pageNuber;
		Map<String, Object> result = WXinterfaceService.findWxCourses(null, shId, pageNuber, pageSize);
		Map<String, String> totals = WXinterfaceService.getCourseTotal(shId, openId);
		view.addObject("dataList", result);
		view.addObject("totals", totals);
		view.addObject("openId",openId);
		view.addObject("shId", shId);
		return view;
	}
	@RequestMapping(value = "getOrderInfo" )
	@ResponseBody
	public Object getOrderInfo(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		String courseId=request.getParameter("courseId");
		if(StringUtils.isBlank(openId) || openId==null){
			/*String	appid =PropertyUtils.getWebServiceProperty("appid");
			String	appSecret =PropertyUtils.getWebServiceProperty("appSecret");

			String code=request.getParameter("code");
			String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			url=url.replace("APPID", appid).replace("SECRET", appSecret).replace("CODE", code);
			HttpRequestData data = UrlUtil.sendGet(url);
			String json=data.getResult();
			JSONObject obj = JSONObject.fromObject(json);
			openId =obj.get("openid").toString();  */
			session.setAttribute("OPENID", openId);
		}
		
		List<Map> result = WXinterfaceService.getOrderList(shId, openId, courseId);
		return result;
	}
	@RequestMapping(value = "getConfirmPay" )
	@ResponseBody
	public Object getConfirmPay() throws Exception {
		String courseSid=request.getParameter("courseSid");
		String result = WXinterfaceService.getConfirmPay(courseSid);
		return result;
	}
	
	//调课通知  获取所有课程
	@RequestMapping(value = "tkNoticeMain" )
	@ResponseBody
	public ModelAndView tkNoticeMain(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/addCourseNotice");
		String pageSize=request.getParameter("pageSize");
		String pageNuber=request.getParameter("pageNuber");
		pageSize=StringUtils.isBlank(pageSize)?"20":pageSize;
		pageNuber=StringUtils.isBlank(pageNuber)?"1":pageNuber;
		Map<String, Object> result = WXinterfaceService.findWxCourses(null, shId, pageNuber, pageSize);
		view.addObject("dataList", result);
		view.addObject("openId",openId);
		view.addObject("shId", shId);
		return view;
	}
	
	@RequestMapping(value = "senTkNoticemessage" )
	@ResponseBody
	public Object senTkNoticemessage(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws ParseException, UnsupportedEncodingException{
		String type=request.getParameter("type");
		String courseName=request.getParameter("currentCourseName");
		String courseId=request.getParameter("course");
		//courseName=java.net.URLDecoder.decode(courseName, "UTF-8");
		String text=request.getParameter("msg");
		List<Map> result = WXinterfaceService.getOrderList(shId, openId, courseId);
		WXinterfaceService.sendTkNoticeMessage(shId,result, courseName, type,text);
		return "true";
	}
	
	@RequestMapping(value = "ccourceList", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView ccourceList(@RequestParam("shId") String shId) throws Exception {
		if(StringUtils.isBlank(shId)){
			shId=PublicWxUtil.getPublicWx().getShId();
		}
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/ccourseList");
		view.addObject("shId", shId);
		return view;
	}
	
	@RequestMapping(value = "ctestList", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView ctestList(@RequestParam("shId") String shId,@RequestParam("openId") String openId) throws Exception {
		//String shId=request.getParameter("shId");
		//String openId=request.getParameter("openId");
		String pageSize=request.getParameter("pageSize");
		String pageNuber=request.getParameter("pageNuber");
		pageSize=StringUtils.isBlank(pageSize)?"20":pageSize;
		pageNuber=StringUtils.isBlank(pageNuber)?"1":pageNuber;
		if(StringUtils.isBlank(shId)){
			shId=PublicWxUtil.getPublicWx().getShId();
		}
		String results=CommonService.isWxManerger(shId, openId);
		ModelAndView view=new ModelAndView("/WEB-INF/page/index/test-List");
		
		
		Map<String, Object> result = WXinterfaceService.findWxCourses(null, shId, pageNuber, pageSize);
		view.addObject("dataList", result);
		view.addObject("shId", shId);
		view.addObject("openId", openId);
		view.addObject("isAdmin", results);
		return view;
	}

}
