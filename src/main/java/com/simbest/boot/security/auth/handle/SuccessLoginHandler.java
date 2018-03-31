/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.handle;

import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.security.auth.model.SysUserInfo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：记录登录日志
 * 作者: lishuyi
 * 时间: 2018/2/25  18:36
 */
@Slf4j
@NoArgsConstructor
@Component
public class SuccessLoginHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("Logged In User " + (SysUserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(ApplicationConstants.HOME_PAGE).forward(request, response);
    }
}
