/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.simbest.exclude.GlobalErrorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.List;

/**
 * 用途：统一异常处理 参考：https://blog.csdn.net/king_is_everyone/article/details/53080851
 * 作者: lishuyi
 * 时间: 2018/5/15  22:26
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(ResourceProperties.class)
public class WebControllerConfiguration {
    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;
    private final ServerProperties serverProperties;

    public WebControllerConfiguration(
            ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    public GlobalErrorController basicErrorController(ErrorAttributes errorAttributes) {
        return new GlobalErrorController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers);
    }

}
