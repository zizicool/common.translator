package com.zhx.common.translator;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.zhx.common.enums.IGradableEnum;
import com.zhx.common.enums.IGroupableEnum;
import com.zhx.common.enums.ILevelEnum;
import com.zhx.common.enums.IValueLabelEnum;

/**
 * 枚举工具类
 * 
 * @author zhx
 * @date 2023年7月14日
 */
@SuppressWarnings("rawtypes")
public class EnumUtils {
	private static final Logger LOG = LoggerFactory.getLogger(EnumUtils.class);

	private EnumUtils() {
		throw new IllegalStateException("Utility class");
	}

	private static final Set<Class> IGNORE = buildIgnore();
	private static final Map<String, Set<Class<? extends IValueLabelEnum>>> CACHED = new HashMap<>();

	private static Set<Class> buildIgnore() {
		Set<Class> res = new HashSet<>();
		res.add(IValueLabelEnum.class);
		res.add(ILevelEnum.class);
		res.add(IGradableEnum.class);
		res.add(IGroupableEnum.class);
		return res;
	}

	/**
	 * 根据包名初始化包下的所有实现了接口IValueLabelEnum的枚举
	 * 
	 * @param pkName
	 */
	public static final void init(String pkName) {
		synchronized (CACHED) {
			if (CACHED.isEmpty()) {
				CACHED.putAll(loadEnumClassToMap(pkName));
			}
		}
	}

	/**
	 * 初始化所有实现了IValueLabelEnum的枚举
	 */
	public static final void init() {
		synchronized (CACHED) {
			if (CACHED.isEmpty()) {
				CACHED.putAll(loadEnumClassToMap(loadAllEnumClass()));
			}
		}
	}

	private static Set<Class<? extends IValueLabelEnum>> loadAllEnumClass() {
		return logTime(() -> {
			ConfigurationBuilder config = new ConfigurationBuilder()
					.addUrls(ClasspathHelper.forClassLoader())
					.useParallelExecutor()
					.filterInputsBy(FilterBuilder.parsePackages("-java,-javax,-sun,-com.sun,-org"));
			Reflections reflections = new Reflections(config);
			return reflections.getSubTypesOf(IValueLabelEnum.class);
		}, "加载所有包下的枚举");
	}

	public static Set<Class<? extends IValueLabelEnum>> loadEnumClass(String pacageName) {
		return logTime(() -> {
			Reflections reflections = new Reflections(pacageName);
			return reflections.getSubTypesOf(IValueLabelEnum.class);
		}, "加载包(" + pacageName + ")下的枚举");
	}

	private static Set<Class<? extends IValueLabelEnum>> logTime(Supplier<Set<Class<? extends IValueLabelEnum>>> sup,
			String mark) {
		Instant start = Instant.now();
		Set<Class<? extends IValueLabelEnum>> res = sup.get();
		Instant end = Instant.now();
		LOG.info("{} 耗时:{} 毫秒, 共加载IValueLabelEnum类:{}", mark, Duration.between(start, end).toMillis(),
				res == null ? 0 : res.size());
		if (LOG.isDebugEnabled()) {
			LOG.debug("加载的IValueLabelEnum类:{}", res);
		}
		return res;
	}

	private static Map<String, Set<Class<? extends IValueLabelEnum>>> loadEnumClassToMap(String packageName) {
		return loadEnumClassToMap(loadEnumClass(packageName));
	}

	private static Map<String, Set<Class<? extends IValueLabelEnum>>> loadEnumClassToMap(
			Set<Class<? extends IValueLabelEnum>> tmpSet) {
		Map<String, Set<Class<? extends IValueLabelEnum>>> res = new HashMap<>();
		Optional.ofNullable(tmpSet).orElse(Collections.emptySet()).stream().filter(t -> !IGNORE.contains(t)).forEach(t -> {
			put2Map(res, t.getSimpleName(), t);
			put2Map(res, t.getName(), t);
			if (IGradableEnum.class.isAssignableFrom(t)) {
				Optional.ofNullable(t.getEnumConstants()).map(Arrays::stream).orElse(Stream.empty()).forEach(item -> {
					IGradableEnum tmpItem = (IGradableEnum) item;
					put2Map(res, tmpItem.getLevelName(), t);
				});
			}
		});
		return res;
	}

	private static void put2Map(Map<String, Set<Class<? extends IValueLabelEnum>>> map, String key,
			Class<? extends IValueLabelEnum> clazz) {
		map.computeIfAbsent(key, k -> {
			return new HashSet<>();
		}).add(clazz);
	}

	/**
	 * 根据枚举名称获取枚举信息,
	 * 
	 * @param enumName    枚举类全名或类名
	 * @param parentValue 上级值, 如果为空则取类型对应的枚举, 如果不为空则只获取父值相等的枚举.
	 * @param groups      组名, 如果 枚举是可分组的 则只获取指定组的枚举, 否则不进行过滤
	 * @return
	 */
	public static List<IValueLabelEnum> obtainEnumFilterByGroupName(String enumName, String parentValue,
			Set<String> groups) {
		return doObtainEnum(enumName, parentValue, true).filter(t -> {
			// 为空不过滤
			if (CollectionUtils.isEmpty(groups)) {
				return true;
			}
			if (t instanceof IGroupableEnum) {
				return ((IGroupableEnum<?>) t).matchAnyGroup(groups);
			} else {
				return true;
			}
		}).collect(Collectors.toList());
	}

	private static Stream<IValueLabelEnum> doObtainEnumByLabel(String enumName, String pinfo, boolean isValue,
			Set<String> labels) {
		// 为空返回空
		if (CollectionUtils.isEmpty(labels)) {
			return Stream.empty();
		}
		return doObtainEnum(enumName, pinfo, isValue).filter(t -> {
			return CollectionUtils.containsAny(t.getPossibleLabels(), labels);
		});
	}

	/**
	 * 根据父枚举值和枚举值获取枚举信息
	 * 
	 * @param enumName    枚举名
	 * @param parentValue
	 * @param value
	 * @return
	 */
	public static Optional<IValueLabelEnum> obtainEnumByValue(String enumName, String parentValue, Object value) {
		return doObtainEnum(enumName, parentValue, true).filter(t -> {
			return Objects.equals(value, t.getValue())
					|| (value != null && t.getValue() != null && t.getValue().toString().equals(value.toString()));
		}).findFirst();
	}

	public static List<IValueLabelEnum> obtainEnumByLabel(String enumName, String parentValue, Set<String> labels) {
		return doObtainEnumByLabel(enumName, parentValue, true, labels).collect(Collectors.toList());
	}

	/**
	 * 根据枚举类名(可不带包名)和父级值 ,和字典label获取字典值
	 * 
	 * @param enumName    枚举类名(可不带包名)
	 * @param parentValue 父级值 可为空
	 * @param label       字典label值
	 * @return
	 */
	public static List<String> obtainEnumValueByLabel(String enumName, String parentValue, Set<String> labels) {
		return doObtainEnumByLabel(enumName, parentValue, true, labels).map(IValueLabelEnum::getValue)
				.map(t -> Objects.toString(t, null)).collect(Collectors.toList());
	}

	@SafeVarargs
	private static <E> HashSet<E> newHashSet(E... items) {
		HashSet<E> res = new HashSet<>();
		if (items != null) {
			Arrays.stream(items).forEach(res::add);
		}
		return res;
	}

	/**
	 * 根据枚举类名(可不带包名)和父级值 ,和字典label获取字典值
	 * 
	 * @param enumName     枚举类名(可不带包名)
	 * @param parentValue  父级值 可为空
	 * @param label        字典label值
	 * @param defaultValue 不存在值时的默认值.
	 * @return
	 */
	public static String obtainSingleEnumValueByLabel(String enumName, String parentValue, String label,
			String defaultValue) {
		return doObtainEnumByLabel(enumName, parentValue, true, newHashSet(label)).map(IValueLabelEnum::getValue)
				.findFirst().map(Object::toString).orElse(defaultValue);
	}

	/**
	 * 根据枚举类名(可不带包名)和父级标签 ,和字典label获取字典值
	 * 
	 * @param enumName    枚举类名(可不带包名)
	 * @param parentLable 父级标签 可为空
	 * @param label       字典label值
	 * @return
	 */
	public static List<String> obtainEnumValueByPELabel(String enumName, String parentLable, Set<String> labels) {
		return doObtainEnumByLabel(enumName, parentLable, false, labels).map(IValueLabelEnum::getValue)
				.map(t -> Objects.toString(t, null)).collect(Collectors.toList());
	}

	/**
	 * 根据枚举类名(可不带包名)和父级标签 ,和字典label获取字典值
	 * 
	 * @param enumName     枚举类名(可不带包名)
	 * @param parentLable  父级标签 可为空
	 * @param label        字典label值
	 * @param defaultValue 不存在值时的默认值.
	 * @return
	 */
	public static String obtainSingleEnumValueByPELabel(String enumName, String parentLable, String label,
			String defaultValue) {
		return doObtainEnumByLabel(enumName, parentLable, false, newHashSet(label)).map(IValueLabelEnum::getValue)
				.findFirst().map(Object::toString).orElse(defaultValue);
	}

	/**
	 * 根据枚举名称获取枚举信息,
	 * 
	 * @param enumName 枚举类全名或类名
	 * @param pinfo    上级值或标签, 如果为空则取类型对应的枚举, 如果不为空则只获取父值相等的枚举.
	 * @param isValue  true 表示pv参数为值
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private static Stream<IValueLabelEnum> doObtainEnum(String enumName, String pinfo, boolean isValue) {
		Assert.hasText(enumName, "枚举名不能为空");
		return (Stream<IValueLabelEnum>) Optional.ofNullable(CACHED.get(enumName)).map(l -> {
			return l.stream().flatMap(c -> {
				return doObtainEnumByParentValueOrLabel(c, pinfo, isValue);
			});
		}).orElse(Stream.empty());
	}

	/**
	 * 返回parent值为给定值的所有枚举, 如果给定值为空,则返回所有枚举(无论给定的枚举类是否为可分级的)
	 * 
	 * @param <K>
	 * @param clazz
	 * @param pv      值或label
	 * @param isValue true 表示pv参数为值
	 * @return
	 */
	private static <K extends IValueLabelEnum> Stream<K> doObtainEnumByParentValueOrLabel(Class<K> clazz, String pv,
			boolean isValue) {
		boolean hasPv = StringUtils.hasText(pv);
		if (!IGradableEnum.class.isAssignableFrom(clazz)) {
			if (hasPv) {
				return Stream.empty();
			} else {
				return Arrays.stream(clazz.getEnumConstants());
			}
		}
		return Optional.ofNullable(clazz.getEnumConstants()).map(Arrays::stream).orElse(Stream.empty()).filter(t -> {
			if (!hasPv) {
				return true;
			}
			IGradableEnum tmp = (IGradableEnum) t;
			return isParentInfo(tmp.getParent(), pv, isValue);
		});
	}

	private static boolean isParentInfo(Object obj, String pv, boolean isValue) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof IValueLabelEnum) {
			IValueLabelEnum parent = (IValueLabelEnum) obj;
			return isValue ? parent.isMyStrValue(pv) : parent.isMyLabel(pv);
		} else {
			if (isValue) {
				return obj.toString().equals(pv);
			} else {
				return false;
			}
		}
	}

}
