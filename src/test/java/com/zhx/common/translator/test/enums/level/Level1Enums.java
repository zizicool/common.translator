package com.zhx.common.translator.test.enums.level;

import com.zhx.common.enums.IValueLabelEnum;

public enum Level1Enums implements IValueLabelEnum<String>{
	
	JIA_DIAN("家电","1"),
	CLOTHING("服装","2");

	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	private Level1Enums(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
}
