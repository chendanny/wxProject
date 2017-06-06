package org.stan.yxgz.util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stan.yxgz.pojo.AccessToken;
import org.stan.yxgz.pojo.Menu;

public class UserUtil {
	private static Logger log = LoggerFactory.getLogger(UserUtil.class);
	public final static String USER_SELECT_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
	//
	public static JSONObject findUserByOpenId(String access_token, String openId) {
		int result = 0;
		String url = USER_SELECT_URL.replace("OPENID", openId);
		USER_SELECT_URL.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = YiXinUtil.httpRequested(url, "GET", null);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));

			}

		}
		System.out.println(jsonObject);
		return jsonObject;

	}

}
