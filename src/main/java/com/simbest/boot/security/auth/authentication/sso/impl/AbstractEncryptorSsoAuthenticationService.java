/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.util.encrypt.AbstractEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 用途：抽象加密名称单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Slf4j
public class AbstractEncryptorSsoAuthenticationService extends AbstractSsoAuthenticationService {

    private AbstractEncryptor encryptor;

    private IAuthService authService;

    public AbstractEncryptorSsoAuthenticationService(IAuthService authService, AbstractEncryptor encryptor) {
        super(authService);
        this.authService = authService;
        this.encryptor = encryptor;
    }

    @Override
    public String decryptUsername(String username) {
        if(StringUtils.isNotEmpty(username)){
            try {
                username = encryptor.decrypt(username);
                log.debug("Actually get username from request with: {}", username);
                return username;
            } catch (Exception e) {
                return null;
            }
        }else{
            return null;
        }
    }

}
