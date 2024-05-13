package com.zhx.common.enums;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.util.StringUtils;




/**
 * @author zhx 2021-05-28
 * 值和描述类型的枚举接口
 * @param <T>
 */
public interface IValueLabelEnum<T> {
	/**
	 * 获取枚举值
	 * @return T
	 */
	T getValue();
	
	/**
	 * 获取枚举描述
	 * @return String
	 */
	String getLabel();
	
	
	
	default Set<String> getPossibleLabels(){
		Set<String> tmp=new HashSet<>();
		tmp.add(this.getLabel());
		return tmp;
	}
	
	
	default boolean isMyLabel(String label) {
		Set<String> tmpSets=this.getPossibleLabels();
		return StringUtils.hasText(label)&&tmpSets!=null&&tmpSets.contains(label.trim());
	}
	
	
	
	/**
	 * 是不是当前枚举的值
	 * @param t T
	 * @return boolean
	 */
	default boolean isMyValue(T t) {
		return t != null && this.getValue().equals(t);
	}
	
	/**
	 * 是不是当前枚举的字符串形式
	 * @param t T
	 * @return boolean
	 */
	default boolean isMyStrValue(String t) {
		return t != null && this.getValue().toString().equals(t);
	}
	
	
	/**
	 * 给定枚举值,转换成对应的枚举,如果枚举值不存在则返回默认枚举
	 * @param <K> 实现了接口IValueLabelEnum的枚举对象
	 * @param <V> 枚举值类型
	 * @param enumValues 枚举集合
	 * @param v 待转换的枚举值 
	 * @param defaultEnum 默认枚举
	 * @return
	 */
	public static  <K extends Enum<K> & IValueLabelEnum<V>, V> K obtainEnumByValue(Class<K> clazz,V v,K defaultEnum) {
		for (K tmp : clazz.getEnumConstants()) {
			if (tmp!=null&& tmp.getValue().equals(v)) {
				return tmp;
			}
		}
		return defaultEnum;
	}
	
	
	/**
	 * 给定枚举值,转换成对应的枚举,如果枚举值不存在则返回NULL
	 * @param <K> 实现了接口IValueLabelEnum的枚举对象
	 * @param <V> 枚举值类型
	 * @param enumValues 枚举集合
	 * @param v 待转换的枚举值 
	 * @return
	 */
	public static  <K extends Enum<K> & IValueLabelEnum<V>, V> K obtainEnumByValue(Class<K> clazz,V v) {
		return obtainEnumByValue(clazz,v,null);
	}
	
	
	
	/**
	 * 给定枚举值,转换成对应的枚举,如果枚举值不存在则返回默认枚举,如果翻译不出来抛出RuntimeException异常
	 * @param <K> 实现了接口IValueLabelEnum的枚举对象
	 * @param <V>枚举值类型
	 * @param clazz  枚举类
	 * @param v    枚举值
	 * @param defaultEnum
	 * @param errMsg 指定RuntimeException异常的错误信息
	 * @return
	 */
	public static  <K extends Enum<K> & IValueLabelEnum<V>, V> K obtainEnumByValueElseEx(Class<K> clazz,V v,K defaultEnum,String errMsg) {
		if(v==null) return defaultEnum;
		for (K tmp : clazz.getEnumConstants()) {
			if (tmp!=null&& tmp.getValue().equals(v)) {
				return tmp;
			}
		}
		throw new RuntimeException(errMsg);
	}
	
	
	
	/**
	 * 判定指定值是否为指定枚举的值
	 * @param <K>
	 * @param <V>
	 * @param clazz
	 * @param v
	 * @return
	 */
	public static <K extends Enum<K> & IValueLabelEnum<V>, V> boolean isAimValue(Class<K> clazz,V v) {
		K tmp=obtainEnumByValue(clazz,v);
		return tmp!=null;
	}
	
	
	
	/**
	 * 给定枚举值,转换成对应的枚举,如果枚举值不存在则返回NULL
	 * @param <K> 实现了接口IValueLabelEnum的枚举对象
	 * @param <V> 枚举值类型
	 * @param enumValues 枚举集合
	 * @param v 待转换的枚举值 
	 * @return
	 */
	public static  <K extends Enum<K> & IValueLabelEnum<?>> K obtainEnumByStringValue(Class<K> clazz,String v) {
		for (K tmp : clazz.getEnumConstants()) {
			if (v!=null && tmp!=null&& tmp.getValue().toString().equals(v)) {
				return tmp;
			}
		}
		return null;
	}
	
	
	public static <K extends Enum<K> & IValueLabelEnum<?>> String obtainEnumLabelByStringValue(Class<K> clazz,String v) {
		return Optional.ofNullable(obtainEnumByStringValue(clazz, v)) 
				.map(t->t.getLabel()).orElse(v);
	}
	
	public static <K extends Enum<K> & IValueLabelEnum<V>, V> String obtainEnumLabelByValue(Class<K> clazz,V v) {
		return Optional.ofNullable(obtainEnumByValue(clazz, v)) 
				.map(t->t.getLabel()).orElse(v==null?null:v.toString());
	}
	
	
	/**
	 * 给定枚举的label,转换成对应的枚举,如果枚举值不存在则返回默认枚举
	 * @param <K> 实现了接口IValueLabelEnum的枚举对象
	 * @param <V> 枚举值类型
	 * @param enumValues 枚举集合
	 * @param v 待转换的枚举值 
	 * @param defaultEnum 默认枚举
	 * @return
	 */
	public static  <K extends Enum<K> & IValueLabelEnum<?>> K obtainEnumByLabel(Class<K> clazz,String v,K defaultEnum) {
		if(v==null) return defaultEnum;
		for (K tmp : clazz.getEnumConstants()) {
			Set<String> tmpSet=tmp.getPossibleLabels();
			if (tmpSet!=null&&tmpSet.contains(v)) {
				return tmp;
			}
		}
		return defaultEnum;
	}
	public static  <K extends Enum<K> & IValueLabelEnum<?>> K obtainEnumByLabel(Class<K> clazz,String v) {
		return obtainEnumByLabel(clazz,v,null);
	}
	
	
	
	
	
}
