/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用途：解决CSRF保护下swagger的POST请求
 * 作者: lishuyi
 * 时间: 2018/5/26  10:58
 */
@Component
public class Swagger2CsrfProtection implements RequestMatcher {

    @Value("${app.swagger.address}")
    private String swaggerUrl;

    private static final Pattern ALLOWED_METHODS =
            Pattern.compile("^(DELETE|PUT|HEAD|TRACE|OPTIONS)$");
    private static final List<String> LOCALHOST_PATTERNS =
            ImmutableList.of("127.0.0.1", "0:0:0:0:0:0:0:1");

    @Override
    public boolean matches(HttpServletRequest request) {
        // CSRF disabled on PUT, HEAD, TRACE, OPTIONS (i.e. enabled for POST, GET)
        if (ALLOWED_METHODS.matcher(request.getMethod()).matches()) return false;
        // CSRF not required on localhost when swagger-ui is referer
        final String remoteHost = request.getRemoteHost();
        final String referer = request.getHeader("Referer");
        if (remoteHost != null && referer != null
                && LOCALHOST_PATTERNS.contains(remoteHost)
                && swaggerUrl.equals(referer)) {
            return false;
        }
        // otherwise, CSRF is required
        return true;
    }
}
