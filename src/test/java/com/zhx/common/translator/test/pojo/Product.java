package com.zhx.common.translator.test.pojo;

import com.zhx.common.translator.annotation.Trans;
import com.zhx.common.translator.test.enums.level.Level2Enums;

public class Product {

	private String type;
	@Trans(dicType = Level2Enums.class,dependentField ="type",dependentOtherField = {"productCata"})
	private String typeDesc;
	
	@Trans(key = "DEP:typeKeyType",dependentField ="type",dependentOtherField = {"productCata"})
	private String typeDesc222;
	
	
	private String colorCode;
	private String colorCodeType;
	private String colorCodeDefi;
	
	@Trans(key = "DEP:colorCodeDefi",dependentField ="colorCode",dependentOtherField = {"colorCodeType"})
	private String colorCodeDesc;
	
	
	
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
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getColorCodeType() {
		return colorCodeType;
	}
	public void setColorCodeType(String colorCodeType) {
		this.colorCodeType = colorCodeType;
	}
	public String getColorCodeDefi() {
		return colorCodeDefi;
	}
	public void setColorCodeDefi(String colorCodeDefi) {
		this.colorCodeDefi = colorCodeDefi;
	}
	public String getColorCodeDesc() {
		return colorCodeDesc;
	}
	public void setColorCodeDesc(String colorCodeDesc) {
		this.colorCodeDesc = colorCodeDesc;
	}
	
	
	
	
	
	
}
