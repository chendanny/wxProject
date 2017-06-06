package org.stan.yxgz.pojo;

import java.util.List;

public class Coachs {
	
	private String id;
	private String name;
	private String desc;
	private String score;
	private String isHot;
	private String state;
	private List<CourseDay> list;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<CourseDay> getList() {
		return list;
	}
	public void setList(List<CourseDay> list) {
		this.list = list;
	}
	
}
