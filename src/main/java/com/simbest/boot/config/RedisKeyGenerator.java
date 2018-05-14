/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.simbest.boot.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
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

    @Value("${server.servlet.contextPath}")
    private String contextPath;

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


    public String getFullKey(Object target, Method method, Object... objects) {
        StringBuilder sb = getNoArgsKey(target, method);
        log.debug("Get no argument key is: {}", sb.toString());
        for (Object obj : objects) {
            sb.append(obj.toString() + ApplicationConstants.AND);
        }
        log.debug("Get full key is: {}", sb.toString());
        return sb.toString();
    }

    /**
     * 返回key值中不带参数的前缀
     * @param target
     * @param method
     * @param objects
     * @return
     */
    public StringBuilder getNoArgsKey(Object target, Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(contextPath + "::");
        sb.append(target.getClass().getName());
        sb.append("::" + method.getName() + "::");
        return sb;
    }
}

