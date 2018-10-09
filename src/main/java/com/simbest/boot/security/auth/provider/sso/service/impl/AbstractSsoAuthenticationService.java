/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider.sso.service.impl;

import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IPermission;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.auth.provider.sso.service.SsoAuthenticationService;
import com.simbest.boot.security.auth.provider.sso.token.KeyTypePrincipal;
import com.simbest.boot.security.auth.provider.sso.token.SsoUsernameAuthentication;
import com.simbest.boot.security.auth.provider.sso.token.UsernamePrincipal;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.security.Principal;
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
    public SsoUsernameAuthentication attemptAuthentication(SsoUsernameAuthentication authentication) {
        log.debug("Retrive username from request with: {}, appcode with {}", authentication.getPrincipal(), authentication.getCredentials());
        if(null != authentication.getPrincipal() && null != authentication.getCredentials()){
            String keyword = decryptUsername(((Principal)authentication.getPrincipal()).getName());
            log.debug("{} get username from request with: {}, appcode with {}", this.getClass().getSimpleName(), keyword, authentication.getCredentials());
            if(StringUtils.isNotEmpty(keyword)) {
                return attemptAuthentication(keyword, authentication);
            } else{
                log.warn(">_<  I am {} decrypt {} failed", this.getClass().getSimpleName(), authentication.toString());
                return null;
            }
        }else{
            log.error(">_< Want use sso authenticate, but something is null");
            return null;
        }
    }

    /**
     * 在应用的人员群组中校验用户是否可以访问
     * @param username
     * @param appcode
     * @return
     */
    public SsoUsernameAuthentication attemptAuthentication(String keyword, SsoUsernameAuthentication authentication) {
        log.debug("Try to check user {} access app {}.", keyword, authentication.getCredentials());
        SsoUsernameAuthentication token = null;
        try {
            IUser authUser = null;
            if(authentication.getPrincipal() instanceof UsernamePrincipal){
                authUser = authService.findByKey(keyword, IAuthService.KeyType.username);
            } else if (authentication.getPrincipal() instanceof KeyTypePrincipal){
                authUser = authService.findByKey(keyword, ((KeyTypePrincipal)authentication.getPrincipal()).getKeyType());
            }
            log.debug("{} Login user is {}", this.getClass().getSimpleName(), authUser);
            if(null != authUser) {
                String username = authUser.getUsername();
                String appcode = authentication.getCredentials().toString();
                if(authService.checkUserAccessApp(username, appcode)) {
                    log.debug("{} check user {} access app {} sucessfully....", this.getClass().getSimpleName(), username, appcode);
                    //追加权限
                    Set<? extends IPermission> appPermission = authService.findUserPermissionByAppcode(username, appcode);
                    if(null != appPermission && !appPermission.isEmpty()) {
                        log.debug("{} will add {} permissions to user {} for app {}", this.getClass().getSimpleName(), appPermission.size(), username, appcode);
                        authUser.addAppPermissions(appPermission);
                        authUser.addAppAuthorities(appPermission);
                    }
                    token = new SsoUsernameAuthentication(authUser, authentication.getCredentials(), authUser.getAuthorities());
                }
            }
        } catch (Exception e){
            log.debug(">_< {} SSO authentication failed from request with user {} for app {}", this.getClass().getSimpleName(), keyword, authentication.getCredentials());
            Exceptions.printException(e);
        }
        return token;
    }

}
