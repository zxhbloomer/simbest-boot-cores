/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 用途：RESTFul 接口安全配置
 * 作者: lishuyi
 * 时间: 2018/1/20  11:24
 */
@EnableWebSecurity
@Order(20)
public class ApiSecurityConfigurer extends AbstractSecurityConfigurer {
    /**
     * REST安全验证器
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
