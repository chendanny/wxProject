package org.stan.yxgz.pojo.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.stan.yxgz.pojo.PublicWx;
import org.stan.yxgz.util.SpringContextUtils;

public class PublicWxUtil extends  SpringContextUtils{
	public static PublicWx getPublicWx() {
		PublicWx wx = null;
		try {
			wx = (PublicWx) ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("publicwx");

		} catch (Exception e) {
			// TODO: handle exception
		}
		if(wx==null){
			//wx=new PublicWx();
		}
		return wx;
	}

}
