package com.zhx.common.translator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.zhx.common.translator.annotation.GValue;
import com.zhx.common.translator.annotation.NoTrans;
import com.zhx.common.translator.annotation.Trans;





/**
 * 翻译器引擎
 * 
 * @author zhx
 *
 */
@Component
public class TranslatorEngine {
	private static final Logger log=LoggerFactory.getLogger(TranslatorEngine.class);
	private static final ConcurrentHashMap<String,Map<String,Field>> TRANS_CLASS_META_CACHE=new ConcurrentHashMap<>();

	@Autowired
	private List<ITranslator> translators=new ArrayList<>();
	
	
	
	
	
	
	public TranslatorEngine() {
		super();
	}

	public List<ITranslator> getTranslators() {
		return translators;
	}

	public void setTranslators(List<ITranslator> translators) {
		this.translators = translators;
	}
	
	public void translateData(Object data) {
		this.translateData(data, null,null);
	}
	
	@SuppressWarnings("rawtypes")
	private void translateData(Object data,HashMap<String,Object> localCacheValue,Map<String, Object> gvMap) {
		if(data==null||data.getClass()==void.class) {
			return ;
		}
		if(Collection.class.isAssignableFrom(data.getClass())) {
			this.translateDataForCollectionObj((Collection)data);
		}else {
			gvMap=gvMap==null?new HashMap<>():gvMap;
			Map<String,Field> waitingTransList= this.obtainTransMeta(data.getClass());
			if(waitingTransList==null||waitingTransList.isEmpty()) {
				return;
			}
			this.buildGlobalValue(data,waitingTransList,gvMap);
			for (Field field : waitingTransList.values()) {
				if(field.isAnnotationPresent(NoTrans.class)||field.isAnnotationPresent(GValue.class)) {
					continue;
				}else if(field.isAnnotationPresent(Trans.class)) {
					this.translateDataForTransAnnotationField(data, field,waitingTransList,localCacheValue,gvMap);
				}else if(Collection.class.isAssignableFrom(field.getType())) {
					this.translateDataForCollectionField(data, field,gvMap);
				}else if(!isPrimitive(field.getType())) {
					this.translateDataForPojoField(data, field,gvMap);
				}
			}
		}
	}
	
	
	
	/**
	 * @param waitingTransList
	 * @param gvMap
	 */
	private void buildGlobalValue(Object data,Map<String, Field> waitingTransList, Map<String, Object> gvMap) {
		for (Field field : waitingTransList.values()) {
			if(field.isAnnotationPresent(GValue.class)) {
				GValue tmp=field.getAnnotation(GValue.class);
				String key=tmp.value();
				gvMap.computeIfAbsent(StringUtils.hasText(key)?key: field.getName(), tk->{
					return this.obtainValue(data, field);
				});
			}
		}
	}

	private void translateDataForPojoField(Object data, Field field, Map<String, Object> gvMap) {
		Object fieldData=this.obtainValue(data, field);
		this.translateData(fieldData,null,gvMap);
	}
	
	/**
	 * 处理集合字段
	 * @param gvMap 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void translateDataForCollectionField(Object data,Field field, Map<String, Object> gvMap ) {
		HashMap<String,Object> cacheValue=new HashMap<>();
		Collection<Object> subDataList=(Collection) this.obtainValue(data, field);
		if(subDataList==null||subDataList.isEmpty()) {
			return;
		}
		for (Object item : subDataList) {
			this.translateData(item,cacheValue,gvMap);
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateDataForCollectionObj(Collection data) {
		HashMap<String,Object> cacheValue=new HashMap<>();
		for (Object item : data) {
			this.translateData(item,cacheValue,null);
		}
	}
	
	private boolean isArray(Object obj) {
		return obj instanceof Iterable || obj instanceof Object[];
	}

	
	/**
	 * 处理注解的字段
	 */
	private void translateDataForTransAnnotationField(Object data,Field field,Map<String, Field> waitingTransList, 
			HashMap<String,Object> cacheValue ,Map<String, Object> gvMap) {
		if(cacheValue==null) {
			cacheValue=new HashMap<>();
		}
		Trans transMeta=field.getAnnotation(Trans.class);
		//原始值存在时不翻译
		if(this.obtainValue(data, field)!=null
				||transMeta.dependentField()==null
				||transMeta.dependentField().trim().equals("")) {
			return;
		}
		Field mainField=waitingTransList.get(transMeta.dependentField().trim());
		Object mainValue=this.obtainValue(data, mainField);
		if(mainValue==null
				||(mainValue instanceof List && ((List<?>) mainValue).isEmpty())
				||(mainValue instanceof Object[] && ((Object[]) mainValue).length==0)
				) {
			return ;
		}
		List<Object> dependentValues=this.obtainDependentOtherValue(data,transMeta,waitingTransList,gvMap);
		//如果存在依赖值则不取缓存
		Object aimValue=null;
		if((dependentValues!=null&&!dependentValues.isEmpty()) 
				|| this.isArray(mainValue)) {
			aimValue=this.transValueWithTranslator(transMeta,mainValue,dependentValues);
		}else {
			String cacheKey=this.buildCacheKey(transMeta,mainValue);
			aimValue=cacheValue.computeIfAbsent(cacheKey, k->this.transValueWithTranslator(transMeta,mainValue,dependentValues));
		}
		if(aimValue==null) {
			aimValue=transMeta.defaultValue();
		}
		if(aimValue!=null) {
			field.setAccessible(true);
			try {
				if(field.getType().equals(String.class)) {
					field.set(data, aimValue.toString());
				}else {
					field.set(data, aimValue);
				}
				
			} catch (Exception e) {
				log.debug("设置翻译值时出错",e);
				//ignore
			}
		}
	}
	
	
	/**
	 * 从翻译器中获取值
	 * @param transMeta
	 * @param mainValue
	 * @param dependentValues
	 * @return
	 */
	private Object transValueWithTranslator(final Trans transMeta, Object mainValue, List<Object> dependentValues) {
		if(this.translators==null) {
			return null;
		}
		if(transMeta.ifStrArray()) {
			mainValue=mainValue.toString().split(transMeta.spacer());
		}
		if(mainValue instanceof Iterable)
		{
			Iterable<?> it=(Iterable<?>)mainValue;
			StringJoiner sj=new StringJoiner(transMeta.aimSpacer());
			for (Object item : it) {
				Object tmp=this._doTransValueWithTranslator(transMeta, item, dependentValues);
				sj.add(tmp==null?null:tmp.toString());
			}
			return sj.toString();
		}else if(mainValue instanceof Object[]) {
			Object[] tmpArray=(Object[])mainValue;
			StringJoiner sj=new StringJoiner(transMeta.aimSpacer());
			for (Object item : tmpArray) {
				Object tmp=this._doTransValueWithTranslator(transMeta, item, dependentValues);
				sj.add(tmp==null?null:tmp.toString());
			}
			return sj.toString();
		}else {
			return this._doTransValueWithTranslator(transMeta, mainValue, dependentValues);
		}
	}
	
	/**
	 * 从翻译器中获取值
	 * @param transMeta
	 * @param mainValue
	 * @param dependentValues
	 * @return
	 */
	private Object _doTransValueWithTranslator(final Trans transMeta, Object mainValue, List<Object> dependentValues) {
		Optional<ITranslator> translator=this.translators.stream().filter(item->{
			return item.isSupport(transMeta);
		}).findFirst();
		return translator.map(t->{
			return t.trans(transMeta, mainValue, dependentValues);
		}).orElse(transMeta.keepOriginal()?mainValue:transMeta.defaultValue());
	}
	
	
	

	/**
	 * 获取依赖值
	 * @param data
	 * @param transMeta
	 * @param waitingTransList
	 * @param gvMap 
	 * @return
	 */
	private List<Object> obtainDependentOtherValue(Object data, Trans transMeta, Map<String, Field> waitingTransList, Map<String, Object> gvMap) {
		if(transMeta.dependentOtherField()==null ||transMeta.dependentOtherField().length==0) {
			return Collections.emptyList();
		}
		List<Object> res=new ArrayList<>();
		for (String fname : transMeta.dependentOtherField()) {
			if(fname!=null&&!fname.trim().equals("")) {
				fname=fname.trim();
				Object tmp=this.obtainValue(data, waitingTransList.get(fname));
				if(tmp==null) {
					tmp=gvMap.get(fname);
				}
				res.add(tmp);
			}else {
				res.add(null);
			}
		}
		return res;
	}
	/**
	 * 根据字段定义获取值
	 * @param data
	 * @param mainField
	 * @return
	 */
	private Object obtainValue(Object data,Field field) {
		if(field==null||data==null) {
			return null;
		}
		try {
			field.setAccessible(true);
			return field.get(data);
		} catch (Exception e) {
			return null;
		}
	}
	
	private String buildCacheKey(Trans transMeta, Object mainValue) {
		StringBuilder sb=new StringBuilder();
		sb.append(transMeta.dicType().getCanonicalName())
			.append("/")
			.append(transMeta.key())
			.append(mainValue);
		return sb.toString();
	}
	
	
	
	
	
	
	

	
	private static boolean isPrimitive(Class<?> clazz) {
		return clazz.isPrimitive()||clazz==String.class
				||clazz==Boolean.class
				||clazz==Character.class
				||Number.class.isAssignableFrom(clazz)
				||Date.class.isAssignableFrom(clazz)
				||clazz.getCanonicalName()==null
				||clazz.getCanonicalName().startsWith("java")
				||clazz.getCanonicalName().startsWith("javax")
				||clazz.getCanonicalName().startsWith("com.alibaba.fastjson")
				||clazz.getCanonicalName().startsWith("org.springframework")
				||clazz.getCanonicalName().equals("char[]") 
				||clazz.isEnum()
				;
	}
	
	
	/**
	 * 获取对象的翻译定义,包含结合类型的字段和通过@Trans进行注解的字段
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Field> obtainTransMeta(Class<?> clazz) {
		if(clazz==null) {
			return Collections.emptyMap();
		}
		String tmpKey=clazz.getCanonicalName();
		if(tmpKey==null) {
			return Collections.emptyMap();
		}	
		return TRANS_CLASS_META_CACHE.computeIfAbsent(tmpKey, key->{
			Map<String,Field> res=new HashMap<>();
			Set<String> dependNames=new HashSet<>();
			Set<Field> allField=ReflectionUtils.getAllFields(clazz);
			for (Field field : allField) {
				if(field.isAnnotationPresent(Trans.class)) {
					Trans tmp=field.getAnnotation(Trans.class);
					if(tmp.dependentField()!=null&&!tmp.dependentField().trim().equals("")) {
						dependNames.add(tmp.dependentField().trim());
					}
					if(tmp.dependentOtherField()!=null) {
						for (String f : tmp.dependentOtherField()) {
							if(f!=null&&!f.trim().equals("")) {
								dependNames.add(f.trim());
							}
						}
					}
					res.put(field.getName(), field);
				}else if(field.isAnnotationPresent(GValue.class)) {
					res.put(field.getName(), field);
				}else if( Collection.class.isAssignableFrom(field.getType())||!isPrimitive(field.getType())) {
					res.put(field.getName(), field);
				}
			} 
			//添加依赖字段
			if(!dependNames.isEmpty()) {
				for (Field field : allField) {
					if(dependNames.contains(field.getName())) {
						res.put(field.getName(), field);
					}
				}
			}
			return  Collections.unmodifiableMap(res);
		});
	}
	
	
	
	

}
