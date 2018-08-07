/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.mochasoft.portal.encrypt.EncryptorUtil;
import com.simbest.boot.base.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * 用途：门户Portal单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Slf4j
@Component
public class MochaSsoAuthenticationServiceImpl extends AbstractSsoAuthenticationService {

    private final static Integer TIMEOUT = 1800;

    @Value("${app.oa.portal.token}")
    private String portalToken;

    @Override
    public String decryptUsername(String username) {
        String decryptUsername = null;
        if(StringUtils.isNotEmpty(username)){
            try {
                decryptUsername = EncryptorUtil.decode(portalToken, username, TIMEOUT);
            } catch (Exception e) {
//                Exceptions.printException(e);
                log.debug("Use {} decrypt username {} to {} faied......", this.getClass().toString(), username, decryptUsername);
            }
        }
        log.debug("Decrypt username {} to {}", username, decryptUsername);
        return decryptUsername;
    }

    public static void main(String[] args) {
        System.out.println(EncryptorUtil.encode("SIMBEST_SSO", "xindanhua"));
    }
}
