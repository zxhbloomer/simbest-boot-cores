/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 用途：抽象过滤器，用于第三方认证扩展
 * 作者: lishuyi
 * 时间: 2018/8/2  19:13
 */
public abstract class CustomAbstractAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAbstractAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }
}
