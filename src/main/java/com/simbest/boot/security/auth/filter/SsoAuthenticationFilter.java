/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.auth.provider.sso.service.SsoAuthenticationService;
import com.simbest.boot.security.auth.provider.sso.token.KeyTypePrincipal;
import com.simbest.boot.security.auth.provider.sso.token.SsoUsernameAuthentication;
import com.simbest.boot.security.auth.provider.sso.token.UsernamePrincipal;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

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

    /**
     * 普通单点方法
     * /findByXXX/sso?loginuser=加密串&appcode=nma
     * /findByXXX/sso?uid=加密串&appcode=nma
     * /findByXXX/sso?keyword=加密串&keytype=keytype&appcode=nma
     *
     * 特殊单点方法-1
     * /findByUsername/sso?loginuser=加密串&username=username明文&appcode=nma
     * /findByUsername/sso?uid=加密串&username=username明文&appcode=nma
     * /findByUsername/sso?keyword=username加密串&keytype=username&username=username明文&appcode=nma
     *
     * 特殊单点方法-2
     * /findByKey/sso?keyword=keyword加密串&keytype=keytype&appcode=nma
     * @param request
     * @return
     */
    protected Principal obtainPrincipal(HttpServletRequest request) {
        Principal principal = null;
        if(StringUtils.isNotEmpty(request.getParameter(AuthoritiesConstants.SSO_API_USERNAME))){
            principal = UsernamePrincipal.builder().username(request.getParameter(AuthoritiesConstants.SSO_API_USERNAME)).build();
        } else if(StringUtils.isNotEmpty(request.getParameter(AuthoritiesConstants.SSO_API_UID))){
            principal = UsernamePrincipal.builder().username(request.getParameter(AuthoritiesConstants.SSO_API_UID)).build();
        } else if(StringUtils.isNotEmpty(request.getParameter(AuthoritiesConstants.SSO_API_KEYWORD))){
            principal = KeyTypePrincipal.builder().keyword(request.getParameter(AuthoritiesConstants.SSO_API_KEYWORD))
                    .keyType(IAuthService.KeyType.valueOf(request.getParameter(AuthoritiesConstants.SSO_API_KEYTYPE))).build();
        }
        return principal;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Principal principal = obtainPrincipal(request);
        String appcode = request.getParameter(AuthoritiesConstants.SSO_API_APP_CODE);
        log.debug("Client will sso access {} with appcode {} and user {}  ", request.getRequestURI(), appcode, principal.getName());
        if (null == principal || StringUtils.isEmpty(appcode)) {
            throw new BadCredentialsException(
                    "Authentication principal can not be invalidate loginuser: " + principal.getName() + " and appcode: " + appcode);
        }

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (authenticationIsRequired(existingAuth, principal)) {
            SsoUsernameAuthentication ssoUsernameAuthentication = new SsoUsernameAuthentication(principal, appcode);
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
    private boolean authenticationIsRequired(Authentication existingAuth, Principal principal) {
        String decryptName = null;
        for(SsoAuthenticationService authService : ssoAuthenticationRegister.getSsoAuthenticationService()) {
            decryptName = authService.decryptUsername(principal.getName());
            if(StringUtils.isNotEmpty(decryptName)) {
                break;
            }
        }

        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        } else if (existingAuth instanceof SsoUsernameAuthentication
                && !existingAuth.getName().equals(decryptName)) {
            return true;
        }
        return false;
    }
}
