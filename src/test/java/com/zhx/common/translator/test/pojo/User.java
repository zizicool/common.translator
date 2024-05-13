package com.zhx.common.translator.test.pojo;

import java.util.List;

import com.zhx.common.translator.annotation.Trans;
import com.zhx.common.translator.test.enums.AgeGroupEnums;
import com.zhx.common.translator.test.enums.ColorEnums;

public class User extends Person{

	
	
	private Person son;
	
	@Trans(dicType = AgeGroupEnums.class,dependentField ="ageGroup",defaultValue = "保密")
	private String ageGroupDesc;
	
	private String colors;
	@Trans(dicType = ColorEnums.class,dependentField ="colors",ifStrArray = true)
	private String colorsDesc;
	
	private List<String> colors2;
	@Trans(dicType = ColorEnums.class,dependentField ="colors2")
	private String colors2Desc;



	public List<String> getColors2() {
		return colors2;
	}

	public void setColors2(List<String> colors2) {
		this.colors2 = colors2;
	}

	public String getColors2Desc() {
		return colors2Desc;
	}

	public void setColors2Desc(String colors2Desc) {
		this.colors2Desc = colors2Desc;
	}

	public String getAgeGroupDesc() {
		return ageGroupDesc;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getColorsDesc() {
		return colorsDesc;
	}

	public void setColorsDesc(String colorsDesc) {
		this.colorsDesc = colorsDesc;
	}

	public void setAgeGroupDesc(String ageGroupDesc) {
		this.ageGroupDesc = ageGroupDesc;
	}

	@Override
	public String toString() {
		return "User [son=" + son + ", ageDesc=" + ageGroupDesc + ", toString()=" + super.toString() + "]";
	}

	public User(String id, String name, String tycoon, String tycoonDesc, int ageGroup,String ageGroupDesc) {
		super(id, name, tycoon, tycoonDesc, ageGroup);
		this.ageGroupDesc = ageGroupDesc;
	}

	public Person getSon() {
		return son;
	}

	public void setSon(Person son) {
		this.son = son;
	}
	
	
	
	
	
	

	
	
	
	
	
}
