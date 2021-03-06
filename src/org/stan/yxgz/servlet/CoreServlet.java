package org.stan.yxgz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.stan.yxgz.pojo.PublicWx;
import org.stan.yxgz.service.CoreService;
import org.stan.yxgz.service.WXinterfaceService;
import org.stan.yxgz.util.PropertyUtils;
import org.stan.yxgz.util.SignUtil;



public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 11111111L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String signature = request.getParameter("signature");

		String timestamp = request.getParameter("timestamp");

		String nonce = request.getParameter("nonce");

		//String shId=request.getParameter("shId");
		String shId=PropertyUtils.getWebServiceProperty("shId");
		String echostr = request.getParameter("echostr");
		request.getSession().setAttribute("signature", signature);
		request.getSession().setAttribute("timestamp", timestamp);
		request.getSession().setAttribute("nonce", nonce);
		request.getSession().setAttribute("echostr", echostr);
		PrintWriter out = response.getWriter();
		request.setAttribute("shId", shId);
		PublicWx wx=WXinterfaceService.getShOperation(shId);
		System.out.println(shId+"-------from database------------token:"+wx.getWxToken());
		if (SignUtil.checkSignature(wx.getWxToken()+"",signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String respMessage = CoreService.processRequest(request,response);
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
		out = null;

	}

}
