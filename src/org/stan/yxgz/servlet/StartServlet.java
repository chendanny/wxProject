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
import org.stan.yxgz.util.TokenThread;
import org.stan.yxgz.util.UrlUtil;
import org.stan.yxgz.util.UrlUtil.HttpRequestData;


public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 11111111L;
	private Logger log=LoggerFactory.getLogger(StartServlet.class);
	public void init() throws ServletException {    
        // 获取web.xml中配置的参数    
        TokenThread.appid = getInitParameter("appid");    
        TokenThread.appSecret = getInitParameter("appsecret");    
    
        log.info("weixin api appid:{}", TokenThread.appid);    
        log.info("weixin api appsecret:{}", TokenThread.appSecret);    
    
        // 未配置appid、appsecret时给出提示    
        if ("".equals(TokenThread.appid) || "".equals(TokenThread.appSecret)) {    
            log.error("appid and appsecret configuration error, please check carefully.");    
        } else {    
            // 启动定时获取access_token的线程    
            new Thread(new TokenThread()).start();    
        }    
    }    
}
