package com.zhx.common.translator.test.enums.level;

import java.util.Set;

public enum JiaDianEnums  implements   Level2Enums{
	
	ICEBOX("冰箱","1"),
	TELEVISION("电视","2");

	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	private JiaDianEnums(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public Level1Enums getParent() {
		return Level1Enums.JIA_DIAN;
	}

	@Override
	public Set<String> getGroups() {
		return null;
	}
	
}
