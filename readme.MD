java 枚举, 业务枚举字典code翻译成label的工具类.实现了以下功能:

1. 局部缓存 . 考虑到翻译集合类型数据时,肯定存在重复code的情况,所以通过局部缓存的方式提高速度. 
2. 支持java枚举, 支持自定义枚举值和标签(名词)
3. 支持自定义翻译源扩展. 示例:见已实现的枚举翻译: com.zhx.common.translator.translators.EnumTranslator


注: 
1. 该组件适合于企业应用,或对性能要求不是很高的应用. 因为 需要对待转换的实体进行循环遍历处理,并进行深度处理(即: 会级联处理每个集合中的类)
