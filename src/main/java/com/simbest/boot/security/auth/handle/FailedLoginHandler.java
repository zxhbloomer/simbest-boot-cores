/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.handle;

import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.constants.ErrorCodeConstants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：记录登录错误日志
 * 作者: lishuyi
 * 时间: 2018/2/25  18:36
 */
@Slf4j
@NoArgsConstructor
@Component
public class FailedLoginHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception != null) {
            if (exception instanceof BadCredentialsException) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
                        new InsufficientAuthenticationException(ErrorCodeConstants.LOGIN_ERROR_BAD_CREDENTIALS));
            } else if (exception instanceof UsernameNotFoundException || exception instanceof InternalAuthenticationServiceException) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
                        new InsufficientAuthenticationException(ErrorCodeConstants.USERNAME_NOT_FOUND));
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(ApplicationConstants.LOGIN_ERROR_PAGE).forward(request, response);
    }
}
