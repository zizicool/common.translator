package com.zhx.common.translator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSON;
import com.zhx.common.translator.annotation.Trans;


/**
 * @author zhx
 * @date 2023年6月8日
 */
public abstract class AbstractTranslator implements ITranslator {
	private static final Logger LOG=LoggerFactory.getLogger(AbstractTranslator.class);
	
	private static final ConcurrentHashMap<String, Map<String, Object >> CACHED_KEYMAP=new ConcurrentHashMap<>();
	/**
	 * 解析key, 如果配置的key为map形式则根据依赖值转换目标key
	 * @param transMeta
	 * @param dependentOtherValues
	 * @return
	 */
	protected  String buildKey(KeyObj ko,Trans transMeta, List<?> dependentOtherValues) {
		String tmpKey=ko.obtainValueAsString();
		if(tmpKey==null||!transMeta.ifMapKey()) {
			return tmpKey;
		}
		if(CollectionUtils.isEmpty(dependentOtherValues)||dependentOtherValues.get(0)==null) {
			return null;
		}
		String keyValue=dependentOtherValues.get(0).toString();
		Map<String, Object > tmap= CACHED_KEYMAP.computeIfAbsent(tmpKey,tmp->{
			try {
				return JSON.parseObject(tmp);
			} catch (Exception e) {
				LOG.error("解析翻译key出错. key:{}",tmp,e);
				return Collections.emptyMap();
			}
		} );
		return Objects.toString(tmap.get(keyValue), null) ;
		
	}

}
