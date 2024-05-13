package com.zhx.common.translator.translators;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.zhx.common.enums.IGradableEnum;
import com.zhx.common.enums.IValueLabelEnum;
import com.zhx.common.translator.EnumUtils;
import com.zhx.common.translator.ITranslator;
import com.zhx.common.translator.annotation.Trans;
/**
 * 实现了IValueLabelEnum接口的枚举翻译器
 * @author zhx 2021年9月13日
 *
 */
@SuppressWarnings(value = {"rawtypes","unchecked"})
@Component
public class EnumTranslator implements ITranslator {
	

	public EnumTranslator() {
		super();
	}
	
	@Override
	public boolean isSupport(Trans transMeta) {
		return IValueLabelEnum.class.isAssignableFrom(transMeta.dicType());
	}

	@Override
	public int order() {
		return 0;
	}

	@Override
	public Object trans(Trans transMeta, Object dependentValue, List<?> dependentOtherValues) {
		Object pv=CollectionUtils.isEmpty(dependentOtherValues)?null:dependentOtherValues.get(0);
		if(IGradableEnum.class.isAssignableFrom(transMeta.dicType())&&pv!=null) {
			return EnumUtils.obtainEnumByValue(transMeta.dicType().getSimpleName(), pv.toString(), dependentValue)
					.map(IValueLabelEnum::getLabel).orElse(null);
		}else if(transMeta.dicType().getEnumConstants()!=null){
			for (Object tmp : transMeta.dicType().getEnumConstants()) {
				IValueLabelEnum<Object> tmpEnumItem=(IValueLabelEnum)tmp;
				if (dependentValue!=null && (tmpEnumItem.isMyStrValue(dependentValue.toString())||tmpEnumItem.isMyValue(dependentValue))) {
					return tmpEnumItem.getLabel();
				}
			}
		}
		return null;
	}

}
