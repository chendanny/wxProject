package org.stan.yxgz.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dt.dtpt.mybatis.model.sijiao.EduCourse;
import com.dt.dtpt.service.sijiao.SijiaoService;
import com.dt.dtpt.util.Result;

@Controller
@RequestMapping("/sijiaoController")
public class SijiaoController {

	/*@Autowired
	SijiaoService sijiaoService;
	
	@RequestMapping("test.do")
	public void test(){
		Result rs = sijiaoService.isWxManerger("123","321");
		System.out.println(rs.isSuccess());
	}
	
	@RequestMapping("test2.do")
	public void test2(){
		Result rs = sijiaoService.findWxCourses("6827b4cf-0eff-4545-9e4f-da8510351fca", new EduCourse(), 1, 10);
		List<HashMap<String, Object>> estrs = (List<HashMap<String, Object>>) rs.getResult();
		
		List<EduCourse> es = (List<EduCourse>) rs.getResult();
		由于rs.getResult为Object对象，此种转换会报错，只能先转为map再操作；
		后期接口不用Result封装，直接返回简单对象集合，就可以直接拿到类似List<EduCourse>数据了
		
		if(estrs != null){			
			for(HashMap<String, Object> map:estrs){
				System.out.println(map.get("courseInfo") + "   " + map.get("courseName") + "    " + map.get("maxStudents"));
			}
		}
	}*/
}
