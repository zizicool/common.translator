package com.zhx.common.translator.test;

import com.zhx.common.translator.EnumUtils;
import com.zhx.common.translator.TranslatorEngine;
import com.zhx.common.translator.test.translator.DictTranslator;
import com.zhx.common.translator.translators.EnumTranslator;

public class AbstractTest {
	
	public static TranslatorEngine getTranslatorEngine() {
		EnumUtils.init();
		TranslatorEngine  engine=new TranslatorEngine();
		engine.getTranslators().add(new EnumTranslator());
		engine.getTranslators().add(new DictTranslator());
		return engine;
	}

}
