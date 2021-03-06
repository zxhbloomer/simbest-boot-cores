/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbest.boot.base.web.response.JsonResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 用途：自定义OAUTH2受保护的资源请求错误入口
 * 作者: lishuyi
 * 时间: 2018/8/29  22:03
 */
public class OauthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws ServletException {
        JsonResponse jsonResponse = JsonResponse.builder()
                .errcode(401)
                .message(authException.getMessage())
                .status(401)
                .timestamp(new Date())
                .path(request.getServletPath())
                .build();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), jsonResponse);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
