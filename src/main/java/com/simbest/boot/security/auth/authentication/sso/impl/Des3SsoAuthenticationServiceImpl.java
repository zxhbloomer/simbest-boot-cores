/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.util.encrypt.Des3Encryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用途：3DES加密名称单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Component
@Slf4j
public class Des3SsoAuthenticationServiceImpl extends AbstractEncryptorSsoAuthenticationService {

    @Autowired
    private Des3Encryptor encryptor;

    @Autowired
    private IAuthService authService;

    public Des3SsoAuthenticationServiceImpl(IAuthService authService, Des3Encryptor encryptor) {
        super(authService, encryptor);
        this.authService = authService;
        this.encryptor = encryptor;
    }


}
