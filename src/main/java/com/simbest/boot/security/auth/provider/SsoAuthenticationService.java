/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider;

import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途：单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
public interface SsoAuthenticationService {

    /**
     * 从请求中获取用户名
     * @param request 验证请求
     * @return 用户名
     */
    String getUsername(HttpServletRequest request);

    /**
     * 根据请求验证是否为合法用户
     * @param request 验证请求
     * @param userDetails 用户信息
     * @return true/false
     */
    Boolean authenticate(HttpServletRequest request, UserDetails userDetails);
}
