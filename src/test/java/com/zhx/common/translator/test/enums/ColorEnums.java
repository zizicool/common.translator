package com.zhx.common.translator.test.enums;

import com.zhx.common.enums.IValueLabelEnum;

public enum ColorEnums implements IValueLabelEnum<String>{
	
	BLACK("黑色","1"),
	WHITE("白色","2"),
	RED("红色","3"),
	GREEN("绿色","4"),
	ORANGE("橙色","5");

	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	private ColorEnums(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
}
