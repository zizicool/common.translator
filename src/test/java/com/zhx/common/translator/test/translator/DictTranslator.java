package com.zhx.common.translator.test.translator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.lang.model.type.NullType;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.zhx.common.translator.AbstractTranslator;
import com.zhx.common.translator.KeyObj;
import com.zhx.common.translator.annotation.Trans;


/**
 * 字典翻译器
 * @author zhx 2021年9月13日
 *
 */
public class DictTranslator extends AbstractTranslator{
	
	
	private static final  Map<Object, List<Map>> DICTS=new HashMap<>();
	static {
		DICTS.put("color1", JSON.parseArray("[{\"code\":'01',\"label\":\"color1-红色\"},{\"code\":'02',\"label\":\"color1-绿色\"}]",
				Map.class));
		DICTS.put("color2", JSON.parseArray("[{\"code\":'01',\"label\":\"color2-褐色\"},{\"code\":'02',\"label\":\"color2-黑色\"}]",
				Map.class));
	}
	
	
	@Override
	public int order() {
		return 0;
	}


	@Override
	public boolean isSupport(KeyObj ko, Trans transMeta) {
		return transMeta.dicType()==NullType.class&&StringUtils.hasText(ko.obtainValueAsString());
	}

	@Override
	public Object trans(KeyObj ko, Trans transMeta, Object dependentValue,
			List<?> dependentOtherValues) {
		if(dependentValue==null) {
			return null;
		}
		String key=this.buildKey(ko,transMeta,dependentOtherValues);
		if(!StringUtils.hasText(key)) {
			return null;
		}
		return Optional.ofNullable(DICTS.get(key))
				.orElse(Collections.emptyList())
				.stream()
				.filter(Objects::nonNull)
				.filter(p->dependentValue.equals(p.get("code")))
				.findFirst()
				.map(p->p.get("label"))
				.orElse(null);
	}

	
	
	
	
	
	

}
