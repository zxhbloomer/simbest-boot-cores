/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util;

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.Exceptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 用途：Map工具类
 * 作者: lishuyi
 * 时间: 2018/6/7  0:48
 */
public class MapUtil {

    /**
     * Map转对象
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * 对象转Map
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if(obj == null)
            return null;
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    /**
     * Map key 排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

}

class MapKeyComparator implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}