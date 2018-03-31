/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.constants.ErrorCodeConstants;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用途：验证码过滤器
 * 作者: lishuyi
 * 时间: 2018/3/7  0:10
 */
public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // 是否开启验证码功能
    private boolean isOpenValidateCode = true;

    public CaptchaAuthenticationFilter() {
        super(new AntPathRequestMatcher(ApplicationConstants.LOGIN_PAGE, HttpMethod.POST.name()));
        SimpleUrlAuthenticationFailureHandler failedHandler = (SimpleUrlAuthenticationFailureHandler) getFailureHandler();
        failedHandler.setDefaultFailureUrl(ApplicationConstants.LOGIN_ERROR_PAGE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!requiresAuthentication(req, res)) {
            chain.doFilter(request, response);
            return;
        }
        if (isOpenValidateCode) {
            if (!checkValidateCode(req, res)) {
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 覆盖授权验证方法，这里可以做一些自己需要的session设置操作
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        return null;
    }

    protected boolean checkValidateCode(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String sessionValidateCode = obtainSessionValidateCode(session);
        String validateCodeParameter = obtainValidateCodeParameter(request);
        if (StringUtils.isEmpty(validateCodeParameter)
                || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
            unsuccessfulAuthentication(request, response,
                    new InsufficientAuthenticationException(ErrorCodeConstants.LOGIN_ERROR_INVALIDATE_CODE));
            return false;
        }
        return true;
    }

    private String obtainValidateCodeParameter(HttpServletRequest request) {
        Object obj = request.getParameter(ApplicationConstants.LOGIN_VALIDATE_CODE);
        return null == obj ? "" : obj.toString();
    }

    private String obtainSessionValidateCode(HttpSession session) {
        Object obj = session.getAttribute(ApplicationConstants.LOGIN_SESSION_CODE);
        // 让上一次的验证码失效
        session.setAttribute(ApplicationConstants.LOGIN_SESSION_CODE, null);
        // 让上一次验证异常失效
        session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, null);
        return null == obj ? "" : obj.toString();
    }
}
