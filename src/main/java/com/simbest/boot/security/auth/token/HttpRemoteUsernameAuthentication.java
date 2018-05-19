/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.token;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 用途：基于UUMS远程SSO单点登录的验证Authentication
 * 作者: lishuyi
 * 时间: 2018/1/20  15:25
 */
public class HttpRemoteUsernameAuthentication extends UsernamePasswordAuthenticationToken {

    public HttpRemoteUsernameAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
