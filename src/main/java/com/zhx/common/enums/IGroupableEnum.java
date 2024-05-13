package com.zhx.common.enums;

import java.util.Set;

import org.springframework.util.CollectionUtils;

/**
 * 可分组枚举
 * @author zhx
 * @date 2023年7月14日
 * @param <T>
 * @param <P>
 */
public interface  IGroupableEnum<T> extends IValueLabelEnum<T> {
	/**
	 * 所属分组
	 * @return
	 */
	public Set<String> getGroups();
	
	/**
	 * 判定当前枚举是否满足给定的分组, 如果枚举未配置分组,会直接返回true.
	 * @param groups
	 * @return
	 */
	default  boolean matchAnyGroup( Set<String> groups) {
		if(CollectionUtils.isEmpty(groups)) {
			return true;
		}
		return CollectionUtils.containsAny(this.getGroups(), groups);
	}
	
}
