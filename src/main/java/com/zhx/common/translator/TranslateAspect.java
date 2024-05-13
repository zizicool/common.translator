package com.zhx.common.translator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 翻译器切面
 * @author zhx 2021年8月31日
 *
 */

@Aspect
@Component
public class TranslateAspect {
	private static final  Logger LOG=LoggerFactory.getLogger(TranslateAspect.class);
	
	private final TranslatorEngine transEngine;
	
	public TranslateAspect(TranslatorEngine te) {
		this.transEngine=te;
	}
	
	
	
	@AfterReturning(
            value = "@annotation(com.chinatower.product.crm.common.core.translator.annotation.DoTrans)",
            returning = "result"
    )
    public void after(JoinPoint jp,Object result) {
       if(result == null || result.getClass() == void.class){
           return;
       }
       if(this.transEngine==null) {
    	   LOG.error("字段转换器未配置");
    	   return ;
       }
        try {
        	this.transEngine.translateData(result);
        } catch (RuntimeException e) {
        	LOG.warn("字段转换出错,目标方法:{}",jp.getSignature().getName(),e);
        }
    }
}
