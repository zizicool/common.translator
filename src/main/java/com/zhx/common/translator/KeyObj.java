package com.zhx.common.translator;

import javax.lang.model.type.NullType;

/**
 * key对象
 * @author zhx 2025年4月21日
 */
public class KeyObj {
	
	/**
	 * 类模式的字典
	 */
	private static final int TYPE_CLASS=1;
	/**
	 * 字符串模式的字典
	 */
	private static final int TYPE_STRING=2;
	
	
	private int type;
	
	private Object value;
	
	
	
	
	public static KeyObj by(String value) {
		KeyObj res=new KeyObj();
		res.setType(TYPE_STRING);
		res.setValue(value);
		return res;
	}
	
	
	public static KeyObj by(Class<?> value) {
		KeyObj res=new KeyObj();
		res.setType(TYPE_CLASS);
		res.setValue(value);
		return res;
	}
	
	public Class<?> obtainValueAsClass(){
		if(this.value==null) {
			return NullType.class ;
		}
		return TYPE_CLASS==this.type?(Class<?>) this.value:NullType.class;
	}

	public int getType() {
		return type;
	}

	private void setType(int type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	private void setValue(Object value) {
		this.value = value;
	}
	
	
	
	
	
}
