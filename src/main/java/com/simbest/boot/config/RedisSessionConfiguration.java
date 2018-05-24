/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

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

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SECURITYID"); // <1>
        //serializer.setCookiePath("/"); // <2>
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // <3>
        return serializer;
    }

}
