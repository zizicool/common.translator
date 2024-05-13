package com.zhx.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 可分级的枚举
 * @author zhx
 * @date 2023年7月14日
 * @param <T>
 * @param <P>
 */
public interface  IGradableEnum<T,P> extends IValueLabelEnum<T> {
	
	/**
	 * 所属父级别
	 * @return
	 */
	public P getParent();
	
	/**
	 * 获取所属等级名称
	 * @return
	 */
	public String getLevelName();
	
	
	default boolean isParentValue(String pv) {
		P p=this.getParent();
		if(p==null) {
			return false;
		}
		if(p instanceof IValueLabelEnum) {
			return ((IValueLabelEnum<?>)p).isMyStrValue(pv);
		}else {
			return p.toString().equals(pv);
		}
	}
	
	
	default boolean isParentLabel(String label) {
		P p=this.getParent();
		if(p==null) {
			return false;
		}
		if(p instanceof IValueLabelEnum) {
			return ((IValueLabelEnum<?>)p).isMyLabel(label);
		}else {
			return false;
		}
	}

	
	
	
	/**
	 * 根据父枚举值获取包含的自枚举
	 * @param <K> 子枚举类
	 * @param <V> 子枚举的枚举值类型
	 * @param <P>  子枚举所属的父级枚举的枚举值
	 * @param clazz
	 * @param parentValue
	 * @return
	 */
	public static  <K extends Enum<K> & IGradableEnum<V,?>, V,P> List<V> obtainEnumValueByParentValue(Class<K> clazz,P parentValue) {
		List<V> res=new ArrayList<>();
		if(parentValue==null) {
			return res;
		}
		for (K tmp : clazz.getEnumConstants()) {
			if (tmp!=null&&tmp.getParent()!=null 
					&& (
							(tmp.getParent() instanceof IValueLabelEnum && ((IValueLabelEnum<?>)tmp.getParent()).isMyStrValue(parentValue.toString()))
							||tmp.getParent().equals(parentValue))
				) {
				res.add(tmp.getValue());
			}
		}
		return res;
	}
	
	
	
	
	
}
