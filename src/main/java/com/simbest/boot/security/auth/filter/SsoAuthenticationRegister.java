/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.security.auth.provider.sso.service.SsoAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 用途：所有单点认证Token注册器
 * 作者: lishuyi
 * 时间: 2018/4/26  15:28
 */
@Component
@Slf4j
public class SsoAuthenticationRegister {
    @Autowired
    private ApplicationContext appContext;


    public Collection<SsoAuthenticationService> getSsoAuthenticationService(){
        Map<String, SsoAuthenticationService> auths = appContext.getBeansOfType(SsoAuthenticationService.class);
        return auths.values();
    }
}
