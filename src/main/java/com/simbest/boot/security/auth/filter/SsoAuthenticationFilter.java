/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.security.auth.provider.SsoUsernameAuthenticationRegister;
import com.simbest.boot.security.auth.token.SsoUsernameAuthentication;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

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

    public SsoAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Setter
    private SsoUsernameAuthenticationRegister ssoRegister;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String username = ssoRegister.getUsername(request);
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException(
                    "Authentication principal can not be null: " + username);
        }

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (authenticationIsRequired(existingAuth, username)) {
            return this.getAuthenticationManager().authenticate(ssoRegister.getToken(request));
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
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        } else if (existingAuth instanceof SsoUsernameAuthentication
                && !existingAuth.getName().equals(username)) {
            return true;
        }
        return false;
    }
}
