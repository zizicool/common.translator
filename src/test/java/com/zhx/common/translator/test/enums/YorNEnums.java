package com.zhx.common.translator.test.enums;

import com.zhx.common.enums.IValueLabelEnum;

public enum YorNEnums implements IValueLabelEnum<String>{
	
	Y("是","Y"),
	N("不是","N");

	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	private YorNEnums(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
}
