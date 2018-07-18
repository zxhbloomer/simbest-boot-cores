/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * 用途：Spring Session with Redis
 * 参考：https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-redis.html
 *      http://blog.didispace.com/spring-session-xjf-2/
 * 作者: lishuyi
 * 时间: 2018/3/7  14:39
 *
 */
public class RedisSessionInitializer extends AbstractHttpSessionApplicationInitializer { // <1>

    public RedisSessionInitializer() {
        super(RedisConfiguration.class); // <2>
    }

}
