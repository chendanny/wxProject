<%@page import="org.stan.yxgz.pojo.PublicWx"%>
<%@page import="org.stan.yxgz.pojo.util.PublicWxUtil"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="org.stan.yxgz.util.PropertyUtils"%>
<%@page import="org.stan.yxgz.util.UrlUtil"%>
<%@page import="org.stan.yxgz.util.UrlUtil.HttpRequestData"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/contextPath.jsp"%>
<%
	
    PublicWx wx=PublicWxUtil.getPublicWx();
    String	appid =wx.getAppId();
	String	appSecret =wx.getAppSecret();

	String code=request.getParameter("code");
	String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	url=url.replace("APPID", appid).replace("SECRET", appSecret).replace("CODE", code);
	HttpRequestData data = UrlUtil.sendGet(url);
	String json=data.getResult();
	JSONObject obj = JSONObject.fromObject(json);
	String openid =obj.get("openid").toString();  
	String urls="/index/ctestList.do?openId="+openid;
	System.out.println("--------ctestList.do--------------------"+urls);

	%>
	
	
	

<script language="javascript">
	function process(url){
		self.location ='${root1}'+ url+"&shId=${shId}";
	}
</script>
<html>
	<head>
		<title></title>
	</head>
	<body onload="javascript: process('<%=urls%>');">
	</body>
</html>
