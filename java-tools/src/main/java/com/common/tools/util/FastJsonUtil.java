package com.common.tools.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * json 序列化/反序列化等工具
 * </p>
 *
 * @author gongliangjun 2019/12/28 6:00 PM
 */
public class FastJsonUtil {

    private static final SerializeConfig config;

    /**
     * @param jsonData
     * @param classType
     * @return 反序列化对象Object
     *
     * @description 转换JSON字符串为对象
     */
    public static <T> T jsonToObject(final String jsonData,
                                     final Class<T> classType) {
        return JSONObject.parseObject(jsonData, classType);
    }

    public static <T> T jsonToObject(final String jsonData,final TypeReference<T> typeReference){
        return JSONObject.parseObject(jsonData,typeReference);
    }

    /**
     *
     * @description 反序列化为json对象
     * @param jsonString
     * @return JSONObject
     */
    public static JSONObject jsonToJSONObject(final String jsonString) {
        return JSON.parseObject(jsonString);
    }

    /**
     * @param object
     * @return json字符串
     *
     * @description 对象序列化为json
     */
    public static String objectToJson(final Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * @param object
     * @return json字符串
     *
     * @description 对象序列化为json，无规范特征处理，详情参考 features 变量定义
     */
    public static String objectToJsonNoFeatures(final Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * @param map
     * @return mapJsonString
     *
     * @description 将map转化为json string
     */
    public static <K, V> String mapToJson(final Map<K, V> map) {
        String jsonString = JSONObject.toJSONString(map);
        return jsonString;
    }

    /**
     * @param jsonString
     * @return json-Map
     *
     * @description json字符串转化为map
     */
    public static <K, V> Map<K, V> jsonToMap(final String jsonString) {
        Map<K, V> map = (Map<K, V>) JSONObject.parseObject(jsonString);
        return map;
    }

    // 转换为数组
    public static <T> Object[] toArray(final String text) {
        return toArray(text, null);
    }

    // 转换为数组
    public static <T> Object[] toArray(final String text,
                                       final Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }

    // 转换为List
    public static <T> List<T> toList(final String text,
                                     final Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    public static Object toBean(final String text) {
        return JSON.parse(text);
    }

    public static <T> T toBean(final String text,
                               final Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    static {
        config = new SerializeConfig();
        // 使用和json-lib兼容的日期输出格式
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    /**
     * SerializerFeature
     */
    private static final SerializerFeature[] features = {
            // 输出空置字段
            SerializerFeature.WriteMapNullValue,
           // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullListAsEmpty,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNullStringAsEmpty,
            //禁止循环引用
            SerializerFeature.DisableCircularReferenceDetect
    };
}
