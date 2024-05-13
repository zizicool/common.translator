package com.zhx.common.translator.test.enums.level;

import com.zhx.common.enums.ILevelEnum;

public interface Level2Enums extends ILevelEnum<String, Level1Enums>{
	
	
	@Override
	default String getLevelName() {
		return Level2Enums.class.getSimpleName();
	}
	
}
