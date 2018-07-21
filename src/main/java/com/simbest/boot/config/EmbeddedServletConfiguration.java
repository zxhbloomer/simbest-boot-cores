/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用途：内部Servlet容器配置信息
 * 作者: lishuyi
 * 时间: 2018/7/21  17:38
 */
@Configuration
public class EmbeddedServletConfiguration {

    @Bean
    public TomcatServletWebServerFactory containerFactory() {
        return new TomcatServletWebServerFactory() {
            protected void customizeConnector(Connector connector) {
                int maxSize = -1;
                super.customizeConnector(connector);
                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
                    /**
                     * 通过设置MaxSwallowSize，解决在发生MaxUploadSizeExceededException异常时，Tomcat不停的尝试上传，导致前端拿不到错误信息
                     * 参考：
                     * http://www.baeldung.com/spring-maxuploadsizeexceeded
                     * https://blog.csdn.net/a349687999/article/details/81120091
                     * https://github.com/Apress/beg-spring-boot-2/blob/master/chapter-10/springboot-thymeleaf-demo/src/main/java/com/apress/demo/config/WebConfig.java
                     */
                    ((AbstractHttp11Protocol <?>) connector.getProtocolHandler()).setMaxSwallowSize(maxSize);
                    logger.info("Set MaxSwallowSize "+ maxSize);
                }
            }
        };

    }

}
