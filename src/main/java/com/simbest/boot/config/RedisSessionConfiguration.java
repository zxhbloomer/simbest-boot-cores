/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 用途：Spring Session with Redis
 * 参考：https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-redis.html
 *      http://blog.didispace.com/spring-session-xjf-2/
 * 作者: lishuyi
 * 时间: 2018/3/7  14:39
 *
 */
@Configuration
public class RedisSessionConfiguration {

    @Autowired
    private AppConfig config;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SECURITYID"); // <1>
        // 目前设计不能设置，否则导致不同应用相同Cookie在检查Session到期时间时报错
        // Failed to deserialize object type; nested exception is java.lang.ClassNotFoundException: com.simbest.boot.uums.role.model.SysRole
//        if(StringUtils.isNotEmpty(config.getCookiePath()))
//            serializer.setCookiePath(config.getCookiePath()); // <2>
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // <3>
        return serializer;
    }

}
