package com.zhx.common.translator.test.pojo;

import com.zhx.common.translator.annotation.Trans;
import com.zhx.common.translator.test.enums.level.Level2Enums;

public class Product {

	private String type;
	@Trans(dicType = Level2Enums.class,dependentField ="type",dependentOtherField = {"productCata"})
	private String typeDesc;
	
	@Trans(key = "DEP:typeKeyType",dependentField ="type",dependentOtherField = {"productCata"})
	private String typeDesc222;
	
	
	private String typeKeyType;
	
	private String price;
	
	
	
	public String getTypeKeyType() {
		return typeKeyType;
	}
	public void setTypeKeyType(String typeKeyType) {
		this.typeKeyType = typeKeyType;
	}
	
	public String getTypeDesc222() {
		return typeDesc222;
	}
	public void setTypeDesc222(String typeDesc222) {
		this.typeDesc222 = typeDesc222;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
	
}
