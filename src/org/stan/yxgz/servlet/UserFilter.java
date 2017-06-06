package org.stan.yxgz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet implementation class userFilter
 */
public class UserFilter implements Filter {
	private static final long serialVersionUID = 1L;
	private FilterConfig config;
	private String checkSessionKey=null;
	private String url=null;
	private List<String> noCheckList=new ArrayList<String>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserFilter() {
        super();
        // TODO Auto-generated constructor stub
    }


	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpreq = (HttpServletRequest) arg0;
		HttpServletResponse response=(HttpServletResponse)arg1;
		HttpSession session=httpreq.getSession();
		
		Object userId=session.getAttribute(checkSessionKey);
		String redirectPath =url;
		session.setAttribute("login_url", redirectPath);
		String jsp=httpreq.getRequestURI();
		
		
		if("".equals(userId) || userId==null){
			
			session.setAttribute("redirectUrl", jsp);
			System.out.println("未登录，需要登录======================redirectUrl:"+redirectPath);
			response.sendRedirect(redirectPath);
		}
		else{
			System.out.println("已经登录过======================userId:"+userId);
			for(String s:noCheckList){
				if(jsp.indexOf(s)<0){    //不需要过滤
					System.out.println("不过滤======================userId:"+userId);
					arg2.doFilter(arg0, arg1);
					return;
				}
			}
			
		}
		
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		this.config=arg0;
		checkSessionKey = config.getInitParameter("checkSessionKey");   
		url=config.getInitParameter("logonStrings");
		String noCheckString=config.getInitParameter("noCheckList");
		if(noCheckString.indexOf(":")>-1){
			String [] str=noCheckString.split(":");
			for(String s:str){
				noCheckList.add(s);
			}
		}else{
			noCheckList.add(noCheckString);
		}
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
