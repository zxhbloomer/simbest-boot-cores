/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.acl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 用途：自定义ACL访问控制
 * 作者: lishuyi
 * 时间: 2018/2/25  21:54
 * 参考:
 * http://blog.csdn.net/swordcenter/article/details/78794351
 * https://docs.spring.io/spring-security/site/docs/current/reference/html/el-access.html
 * 1.默认实现
 * http://www.baeldung.com/spring-security-create-new-custom-security-expression
 * http://www.baeldung.com/spring-security-acl
 * 2.基于策略
 * https://github.com/lordlothar99/strategy-spring-security-acl
 * 3.完全自定义
 * https://github.com/BerryCloud/spring-data-jpa-acl
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new AclPermissionEvaluator());
        return expressionHandler;
    }


}
