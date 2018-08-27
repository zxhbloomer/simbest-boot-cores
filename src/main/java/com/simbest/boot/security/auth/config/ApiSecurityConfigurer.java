/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.config;

import com.simbest.boot.base.service.IOauth2ClientDetailsService;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.auth.authentication.Oauth2RedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 用途：RESTFul 接口安全配置
 * 参考：
 * http://blog.didispace.com/spring-security-oauth2-xjf-1/
 * http://niocoder.com/2018/04/29/Spring-Boot-2.0-%E6%95%B4%E5%90%88-Spring-Security-Oauth2/
 * https://github.com/longfeizheng/springboot2.0-oauth2
 * 作者: lishuyi
 * 时间: 2018/1/20  11:24
 *
 * 获取token请求（/oauth/token），请求所需参数：client_id、client_secret、grant_type
 * client模式：http://localhost:8080/uums/oauth/token?grant_type=client_credentials&scope=all&client_id=password_changer&client_secret=e10adc3949ba59abbe56e057f20f883e
 * password模式： http://localhost:8080/uums/oauth/token?grant_type=password&scope=all&client_id=password_changer&client_secret=e10adc3949ba59abbe56e057f20f883e&username=hadmin&password=e10adc3949ba59abbe56e057f20f883e
 *
 * 检查token是否有效（/oauth/check_token），请求所需参数：token
 * http://localhost:8080/uums/oauth/check_token?token=f57ce129-2d4d-4bd7-1111-f31ccc69d4d1
 *
 * 注意：client模式没有refresh_token
 * 刷新token请求（/oauth/token），请求所需参数：grant_type、refresh_token、client_id、client_secret
 * http://localhost:8080/uums/oauth/token?grant_type=refresh_token&client_id=password_changer&client_secret=e10adc3949ba59abbe56e057f20f883e&refresh_token=fbde81ee-f419-42b1-1234-9191f1f95be9
 */
@Configuration
@EnableAuthorizationServer
@Order(20)
public class ApiSecurityConfigurer {

    @Autowired
    private IAuthService authService;

    @Autowired
    private Oauth2RedisTokenStore redisTokenStore;

    @Autowired
    private IOauth2ClientDetailsService oauth2ClientDetailsService;

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("*").stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/**/api/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        RedisConnectionFactory redisConnectionFactory;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(oauth2ClientDetailsService);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            // 配置token获取和验证时的策略
            security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            // 配置tokenStore，保存到redis缓存中
            endpoints.authenticationManager(authenticationManager)
                    .tokenStore(redisTokenStore)
                    // 不添加userDetailsService，刷新access_token时会报错
                    .userDetailsService(authService);
        }
    }


}
