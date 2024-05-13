package com.zhx.common.translator;

import java.util.List;

import com.zhx.common.translator.annotation.Trans;


/**
 * 翻译器接口，实现自定义翻译器时实现该接口即可。
 * @author zhx
 *
 */
public interface ITranslator {
	
	/**
	 * 判定当前翻译器是否支持指定类型
	 * @param transMeta
	 * @return
	 */
	boolean isSupport(Trans transMeta);
	
	/**
	 * 返回翻译器的执行顺序。注: 当前设计为只获取第一个支持的翻译器进行翻译，
	 * @return
	 */
	int order();
	/**
	 * 翻译值
	 * @param key  资源关键字
	 * @param keyClass 资源关键子类， key 和keyClass有其一即可
	 * @param dependentValue  依赖的字段值
	 * @param dependentOtherValues 依赖的其它字段值
	 * @return
	 */
	Object trans(Trans transMeta,Object dependentValue, List<?> dependentOtherValues);
	

}
