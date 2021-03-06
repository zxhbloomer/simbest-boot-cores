/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.entryPoint;

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.util.json.JacksonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：无权限认证入口
 * 作者: lishuyi
 * 时间: 2018/12/7  20:02
 */
public class AccessDeniedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript;charset=utf-8");
        response.getWriter().print(JacksonUtils.obj2json(JsonResponse.unauthorized()));
    }
}
