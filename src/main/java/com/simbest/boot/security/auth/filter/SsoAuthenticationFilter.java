/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.security.auth.authentication.sso.SsoAuthenticationService;
import com.simbest.boot.security.auth.authentication.token.SsoUsernameAuthentication;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：单点登录拦截器
 * 作者: lishuyi
 * 时间: 2018/1/20  15:05
 */
//@WebFilter(filterName = "ssoAuthenticationFilter", urlPatterns = "/sso/*")
@Slf4j
public class SsoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private SsoAuthenticationRegister ssoAuthenticationRegister;

    public SsoAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String loginuser = request.getParameter(AuthoritiesConstants.SSO_API_USERNAME);
        String appcode = request.getParameter(AuthoritiesConstants.SSO_API_APP_CODE);
        if (StringUtils.isEmpty(loginuser) || StringUtils.isEmpty(appcode)) {
            throw new BadCredentialsException(
                    "Authentication principal can not be null: " + loginuser);
        }

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (authenticationIsRequired(existingAuth, loginuser)) {
            SsoUsernameAuthentication ssoUsernameAuthentication = new SsoUsernameAuthentication(loginuser, appcode);
            return this.getAuthenticationManager().authenticate(ssoUsernameAuthentication);
        }
        return existingAuth;
    }

    /**
     * 判断单点用户名是否需要验证
     *
     * @param username 用户名
     * @return true/false
     */
    private boolean authenticationIsRequired(Authentication existingAuth, String username) {
        for(SsoAuthenticationService authService : ssoAuthenticationRegister.getSsoAuthenticationService()) {
            String decryptUsername = authService.decryptUsername(username);
            if(StringUtils.isNotEmpty(decryptUsername)) {
                username = decryptUsername;
                break;
            }
        }

        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        } else if (existingAuth instanceof SsoUsernameAuthentication
                && !existingAuth.getName().equals(username)) {
            return true;
        }
        return false;
    }
}
