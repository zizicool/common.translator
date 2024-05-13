package com.zhx.common.translator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.lang.model.type.NullType;

/**
 * 字段注解，用于表示哪些字段需要翻译
 * @author zhx
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Trans {
	/**
	 * 表示字段翻译值的来源类型
	 * @return
	 */
	Class<?> dicType() default NullType.class;
	/**
	 * 表示待翻译的code所在 字典(常量)组的key,和dicType()使用其一即可
	 * 如果ifMapKey()=true时 key值为一个JSON字符串,格式为: {"key1":"xxx",""key2":""}. 
	 *  会根据dependentOtherField()第一个字段的值作为key 在map中获取对应的值作为枚举字典.
	 *  例如: 当我们存在2个颜色字典时:  
	 *  	color1=[{"code":'01',"label":"红色"},{"code":'02',"label":"绿色"}]
	 *  	color2=[{"code":'01',"label":"褐色"},{"code":'02',"label":"黑色"}]
	 *  如果翻译的的属性 color='01' 时, 需要根据另一个字段type(可用值为: 01,02)的值决定使用那个枚举.
	 *  按以下方式配置即可:  key="{"01":"color1",""02":"color2"}" dependentOtherField="type"
	 * 	
	 * @return
	 */
	String key() default "";
	/**
	 * key()值是否为map,
	 * @return
	 */
	boolean ifMapKey() default false;
	/**
	 * 依赖的主字段名称,即:待翻译的code的属性名称
	 * @return
	 */
	String dependentField();
	
	/**
	 * 表示翻译当前字段时，所依赖的其它字段名称. 需要多个字段的code进行组合翻译时使用.
	 * @return
	 */
	String[] dependentOtherField() default {};
	/**
	 * 当翻译不出来值时使用的默认值
	 * @return
	 */
	String defaultValue() default "-";
	
	
	/**
	 * dependentField()设置的字段值是否为字符串数组.例如:  101,202,303
	 * @return
	 */
	boolean ifStrArray() default false;
	/**
	 * 如果为字符串数组时,使用的间隔符号.默认为:逗号","
	 * @return
	 */
	String spacer() default ",";
	
	/**
	 * 翻译后使用的间隔符号
	 * @return
	 */
	String aimSpacer() default ",";
	
	/**
	 * 翻译后的值为Null时,是否显示原始值. true表示显示原始值, false 时会使用defaultValue
	 * @return
	 */
	boolean keepOriginal() default true; 
	
	

	
}
