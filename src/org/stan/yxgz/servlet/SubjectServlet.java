package org.stan.yxgz.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class SubjectServlet extends HttpServlet {
	private static final long serialVersionUID = 11111111L;
	private Logger log=LoggerFactory.getLogger(SubjectServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
	    response.setCharacterEncoding("utf-8");
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getContextPath();
		response.setContentType("text/html");
	    response.setCharacterEncoding("UTF-8");
		String userId=request.getParameter("userId");
		String id=request.getParameter("id");
		//String password =request.getParameter("txtPassword");
		request.setAttribute("params", "广州");
		request.getRequestDispatcher("reportTree/subject-001.jsp").forward(request, response);
		
	}

	
	
	private static String encode(String str) {
		try {
			if (StringUtils.isNotBlank(str)) {
				str = java.net.URLEncoder.encode(str, "UTF-8");
			} else {
				str = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
