package com.zhx.common.translator.test.enums;

import com.zhx.common.enums.IValueLabelEnum;

public enum AgeGroupEnums implements IValueLabelEnum<Integer>{
	
	KIDS("小孩",1),
	YOUNGSTER("年轻人",2),
	MIDDLE_AGER("中年人",3),
	OLDER("老年人",4);

	private String label;
	private Integer value;

	public String getLabel() {
		return label;
	}

	public Integer getValue() {
		return value;
	}

	private AgeGroupEnums(String label, Integer value) {
		this.label = label;
		this.value = value;
	}
	
}
