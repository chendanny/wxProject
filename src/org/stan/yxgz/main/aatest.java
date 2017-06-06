package org.stan.yxgz.main;

import org.apache.commons.lang.math.RandomUtils;
import org.stan.yxgz.util.YiXinUtil;

public class aatest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//YiXinUtil.templateMessage("");
		System.out.println("2a1fec0cdcc04dd1880e80e1c2354a7c");
		excuteString("2a1fec0cdcc04dd1880e80e1c2354a7c");
		String money="0.11";
		if(money.startsWith("0")){
			float sessionmoney=Float.parseFloat(money)*100;
			System.out.println((int)sessionmoney);
			String aa=String.format("%.2f", sessionmoney);
			System.out.println(aa);
		}else{
		float sessionmoney = Float.parseFloat(money);
		System.out.println(sessionmoney);
		String finalmoney = String.format("%.2f", sessionmoney);
		finalmoney = finalmoney.replace(".", "");
		System.out.println(finalmoney);
		}
	}

	
	public static String excuteString(String out_trade_no){
		char [] ch=out_trade_no.toCharArray();
		int a=RandomUtils.nextInt(30);
		System.out.println(a);
		int i=0;
		StringBuffer b=new StringBuffer();
		for(char c:ch){
			if(i==a){
				b.append("8");
			}else{
				b.append(c);
			}
			i++;
		}
		System.out.println(b.toString());
	return b.toString();
	}
	
}
