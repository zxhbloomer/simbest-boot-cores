/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.security.auth.authentication.sso.SsoAuthenticationService;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import com.simbest.boot.security.auth.authentication.token.SsoUsernameAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private SysUserInfoFullService sysUserInfoService;

    /**
     * 尝试从HttpServletRequest获取认证用户名
     * @param request
     * @return
     */
    public String retriveFindUsername(HttpServletRequest request){
        Map<String, SsoAuthenticationService> auths = appContext.getBeansOfType(SsoAuthenticationService.class);
        for(SsoAuthenticationService auth : auths.values()){
            String username = auth.getUsername(request);
            if(StringUtils.isNotEmpty(username)) {
               return username;
            }
        }
        return null;
    }

    /**
     * 尝试从HttpServletRequest获取认证Token
     * @param request
     * @return
     */
    public SsoUsernameAuthentication retriveMakeToken(HttpServletRequest request){
        Map<String, SsoAuthenticationService> auths = appContext.getBeansOfType(SsoAuthenticationService.class);
        for(SsoAuthenticationService auth : auths.values()){
            UserDetails userDetails = sysUserInfoService.loadUserByUsername(retriveFindUsername(request));
            if (userDetails != null) {
                return new SsoUsernameAuthentication(userDetails.getUsername(), userDetails.getPassword());
            } else {
                break;
            }
        }
        return null;
    }

}
