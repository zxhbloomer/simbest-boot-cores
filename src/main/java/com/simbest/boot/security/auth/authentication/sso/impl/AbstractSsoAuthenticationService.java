/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IPermission;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.SimplePermission;
import com.simbest.boot.security.auth.authentication.sso.SsoAuthenticationService;
import com.simbest.boot.security.auth.authentication.token.SsoUsernameAuthentication;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.uums.api.permission.UumsSysPermissionApi;
import com.simbest.boot.uums.api.user.UumsSysUserinfoApi;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

/**
 * 用途：抽象SSO单点认证服务
 * 作者: lishuyi
 * 时间: 2018/6/13  18:02
 */
@Slf4j
@NoArgsConstructor
public abstract class AbstractSsoAuthenticationService implements SsoAuthenticationService {

    @Autowired
    protected IAuthService authService;

    /**
     * 尝试进行认证，抽象父类调用子类decryptUsername子类解密用户名，构建SsoUsernameAuthentication提交authentication
     * @param authentication
     * @return
     */
    @Override
    public SsoUsernameAuthentication attemptAuthentication(Authentication authentication) {
        log.debug("Retrive username from request with: {}, appcode with {}", authentication.getPrincipal(), authentication.getCredentials());
        if(null != authentication.getPrincipal() && null != authentication.getCredentials()
                && StringUtils.isNotEmpty(authentication.getPrincipal().toString())
                && StringUtils.isNotEmpty(authentication.getCredentials().toString())){

            String username = decryptUsername(authentication.getPrincipal().toString());
            log.debug("Actually get username from request with: {}, appcode with {}", username, authentication.getCredentials().toString());
            if(StringUtils.isNotEmpty(username)) {
                return ssoAuthentication(username, authentication.getCredentials().toString());
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
    public SsoUsernameAuthentication ssoAuthentication(String username, String appcode) {
        log.debug("Try to check user {} access app {}.", username, appcode);
        SsoUsernameAuthentication token = null;
        try {
            IUser authUser = authService.findByUsername(username);
            log.debug("Login user is {}", authUser.toString());
            if(null != authUser) {
                if(authService.checkUserAccessApp(username, appcode)) {
                    log.debug("Check user {} access app {} sucessfully....", username, appcode);
                    //追加权限
                    Set<? extends IPermission> appPermission = authService.findUserPermissionByAppcode(username, appcode);
                    if(null != appPermission && !appPermission.isEmpty()) {
                        log.debug("Will add {} permissions to user {} for app {}", appPermission.size(), username, appcode);
                        authUser.addAppPermissions(appPermission);
                        authUser.addAppAuthorities(appPermission);
                    }
                    token = new SsoUsernameAuthentication(authUser, authUser.getAuthorities());
                }
            }
        } catch (Exception e){
            log.debug("SSO authentication failed from request with user {} for app {}", username, appcode);
            Exceptions.printException(e);
        }
        return token;
    }

}
