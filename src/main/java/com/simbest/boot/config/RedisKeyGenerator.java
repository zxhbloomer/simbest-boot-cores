/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.simbest.boot.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 用途：自定义Redis Key值生成器
 * 作者: lishuyi
 * 时间: 2018/5/12  18:11
 */
@Component
@Slf4j
public class RedisKeyGenerator implements KeyGenerator {

    @Value("${spring.cache.redis.key-prefix}")
    private String prefix;

    /**
     * 返回带参数，完整的key值
     * @param target
     * @param method
     * @param objects
     * @return
     */
    @Override
    public Object generate(Object target, Method method, Object... objects) {
        return getFullKey(target, method, objects);
    }


    /**
     * 按照实体、方法、参数生成Redis缓存的Key键
     * @param target
     * @param method
     * @param objects
     * @return
     */
    public String getFullKey(Object target, Method method, Object... objects) {
        StringBuilder sb = getNoArgsKey(target, method);
        log.debug("Get no argument key is: {}", sb.toString());
        if(null != objects && objects.length > 0){
            sb.append(ApplicationConstants.COLON);
        }
        for (Object obj : objects) {
            sb.append(obj.toString() + ApplicationConstants.AND);
        }
        String runtimeKey = StringUtils.removeEnd(sb.toString(), ApplicationConstants.AND);
        log.debug("Get full key is: {}", runtimeKey);
        return runtimeKey;
    }

    /**
     * 按照实体、方法生成Redis缓存的Key键
     * @param target
     * @param method
     * @param objects
     * @return
     */
    public StringBuilder getNoArgsKey(Object target, Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(target.getClass().getName() + ApplicationConstants.COLON);
        sb.append(method.getName());
        return sb;
    }
}

