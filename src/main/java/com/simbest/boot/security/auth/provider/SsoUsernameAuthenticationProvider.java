/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.auth.provider.sso.service.SsoAuthenticationService;
import com.simbest.boot.security.auth.provider.sso.token.KeyTypePrincipal;
import com.simbest.boot.security.auth.provider.sso.token.SsoUsernameAuthentication;
import com.simbest.boot.security.auth.filter.SsoAuthenticationRegister;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 用途：基于用户名的认证器
 * 作者: lishuyi
 * 时间: 2018/1/20  17:49
 */
@Slf4j
@Component
public class SsoUsernameAuthenticationProvider implements AuthenticationProvider {

    @Setter @Getter
    protected boolean hideUserNotFoundExceptions = false;

    @Autowired
    private SsoAuthenticationRegister ssoAuthenticationRegister;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Collection<SsoAuthenticationService> ssoAuthenticationServices = ssoAuthenticationRegister.getSsoAuthenticationService();
        SsoUsernameAuthentication successToken = null;
        for(SsoAuthenticationService authService : ssoAuthenticationServices) {
            log.info("SsoAuthenticationService {} attempt authentication with authentication {}",authService.getClass().getName(), authentication.toString());
            successToken = authService.attemptAuthentication((SsoUsernameAuthentication) authentication);
            if(null != successToken) {
                log.warn("-_- SsoAuthenticationService {} attempt authentication with authentication {} successfully......, get successToken {}",
                        authService.getClass().getName(), authentication.toString(), successToken.toString());
                break;
            }
            log.warn("-_- SsoAuthenticationService {} attempt authentication with authentication {} failed......",authService.getClass().getName(), authentication.toString());
        }
        if (null != successToken) {
            return successToken;
        } else {
            log.error(">_< SSO authentication failed, with sso token {}", authentication.toString());
            throw new
                    BadCredentialsException("SSO authentication failed, with sso token " + authentication.toString());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(SsoUsernameAuthentication.class);
    }
}
