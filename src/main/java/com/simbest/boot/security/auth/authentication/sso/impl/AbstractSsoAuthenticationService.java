/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.auth.authentication.sso.SsoAuthenticationService;
import com.simbest.boot.security.auth.authentication.token.SsoUsernameAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

/**
 * 用途：抽象SSO单点认证服务
 * 作者: lishuyi
 * 时间: 2018/6/13  18:02
 */
@Slf4j
public abstract class AbstractSsoAuthenticationService implements SsoAuthenticationService {

    private IAuthService authService;

    public AbstractSsoAuthenticationService(IAuthService authService){
        this.authService = authService;
    }

    /**
     * 尝试进行认证，抽象父类调用子类decryptUsername子类解密用户名，构建SsoUsernameAuthentication提交authentication
     * @param authentication
     * @return
     */
    public SsoUsernameAuthentication attemptAuthentication(Authentication authentication) {
        log.debug("Retrive username from request with: {}, appcode with {}", authentication.getPrincipal(), authentication.getCredentials());
        if(null != authentication.getPrincipal() && null != authentication.getCredentials()
                && StringUtils.isNotEmpty(authentication.getPrincipal().toString())
                && StringUtils.isNotEmpty(authentication.getCredentials().toString())){

            String username = decryptUsername(authentication.getPrincipal().toString());
            if(StringUtils.isNotEmpty(username)) {
                log.debug("Actually get username from request with: {}", username);
                return authentication(username, authentication.getCredentials().toString());
            } else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 在应用的人员群组中校验用户是否可以访问
     * @param username
     * @param appcode
     * @return
     */
    public SsoUsernameAuthentication authentication(String username, String appcode) {
        SsoUsernameAuthentication token = null;
        try {
            IUser authUser = authService.findByUsername(username);
            if(null != authUser) {
                if(authService.checkUserAccessApp(username, appcode)) {
                    token = new SsoUsernameAuthentication(authUser, authUser.getAuthorities());
                }
            }
        } catch (Exception e){
            Exceptions.printException(e);

        }
        return token;
    }

}
