package com.zhx.common.translator.test.pojo;

import com.zhx.common.translator.annotation.Trans;
import com.zhx.common.translator.test.enums.YorNEnums;

public class Person {
	private String id;
	private String name;
	private String tycoonFlag;
	
	@Trans(dicType =  YorNEnums.class,dependentField ="tycoonFlag",defaultValue = "未填写")
	private String tycoonDesc;
	
	private int ageGroup;
	
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
	public String getTycoonFlag() {
		return tycoonFlag;
	}
	public void setTycoonFlag(String tycoonFlag) {
		this.tycoonFlag = tycoonFlag;
	}
	public String getTycoonDesc() {
		return tycoonDesc;
	}
	public void setTycoonDesc(String tycoonDesc) {
		this.tycoonDesc = tycoonDesc;
	}
	
	
	public int getAgeGroup() {
		return ageGroup;
	}
	public void setAgeGroup(int ageGroup) {
		this.ageGroup = ageGroup;
	}
	public Person(String id, String name, String tycoonFlag, String tycoonDesc, int ageGroup) {
		super();
		this.id = id;
		this.name = name;
		this.tycoonFlag = tycoonFlag;
		this.tycoonDesc = tycoonDesc;
		this.ageGroup = ageGroup;
	}
	
	
	
	
	
	
	
}
