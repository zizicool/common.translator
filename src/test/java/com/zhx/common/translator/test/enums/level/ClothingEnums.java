package com.zhx.common.translator.test.enums.level;

import java.util.Set;

public enum ClothingEnums implements   Level2Enums{
	
	OVERCOAT("外套","1"),
	TROUSERS("裤子","9");

	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	private ClothingEnums(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public Level1Enums getParent() {
		return Level1Enums.CLOTHING;
	}

	@Override
	public Set<String> getGroups() {
		return null;
	}
	
}
