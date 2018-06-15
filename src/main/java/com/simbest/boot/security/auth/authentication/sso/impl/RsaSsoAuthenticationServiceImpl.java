/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用途：RSA加密名称单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Component
@Slf4j
public class RsaSsoAuthenticationServiceImpl extends AbstractSsoAuthenticationService {

    @Autowired
    private RsaEncryptor encryptor;

    @Autowired
    private IAuthService authService;

    @Autowired
    public RsaSsoAuthenticationServiceImpl(IAuthService authService) {
        super(authService);
        this.authService = authService;
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
