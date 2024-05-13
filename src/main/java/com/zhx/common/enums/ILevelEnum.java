package com.zhx.common.enums;

/**
 * 层级字典模型接口
 * 
 * @author zhx
 * @date 2023年7月12日
 */
public interface ILevelEnum<T, P> extends IGradableEnum<T, P>, IGroupableEnum<T> {

	@Override
	default String getLevelName() {
		return this.getClass().getSimpleName();
	}

}
