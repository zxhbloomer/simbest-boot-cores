/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * 用途：配置静态文件目录
 * 作者: lishuyi
 * 时间: 2018/4/17  23:28
 */
@Configuration
@EnableWebMvc
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver versionResourceResolver = new VersionResourceResolver()
                .addVersionStrategy(new ContentVersionStrategy(), "/**");

        registry.addResourceHandler(
                "/webjars/**",
                "/img/**",
                "/images/**",
                "/css/**",
                "/js/**",
                "/fonts/**",
                "/html/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/static/img/",
                        "classpath:/static/images/",
                        "classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/fonts/",
                        "classpath:/static/html/")
                .setCachePeriod(-1) /* no cache */
                .resourceChain(true)
                .addResolver(versionResourceResolver);
    }
}
