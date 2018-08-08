/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider.sso.service;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.auth.provider.sso.token.SsoUsernameAuthentication;
import org.springframework.security.core.Authentication;

/**
 * 用途：单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
public interface SsoAuthenticationService {

    /**
     * 解密请求中的用户名
     * @param request 验证请求
     * @return 用户名
     */
    String decryptUsername(String username);

    /**
     * 初级认证
     * @param authentication
     * @return
     */
    SsoUsernameAuthentication attemptAuthentication(Authentication authentication, IAuthService.KeyType keyType);

}
