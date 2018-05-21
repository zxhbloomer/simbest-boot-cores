/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.simbest.boot.security.auth.authentication.sso.SsoAuthenticationService;
import com.simbest.boot.security.auth.repository.SysUserInfoFullRepository;
import com.simbest.boot.util.encrypt.Des3Encryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途：3DES加密名称单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Component
@Slf4j
public class Des3SsoAuthenticationServiceImpl implements SsoAuthenticationService {
    @Autowired
    private Des3Encryptor encryptor;

    @Autowired
    private SysUserInfoFullRepository userRepository;

    /**
     * 从请求中获取用户名
     * @param request 验证请求
     * @return 用户名
     */
    public String getUsername(HttpServletRequest request) {
        String username = request.getParameter("username");
        log.debug("Retrive username from request with: {}", username);
        if(StringUtils.isNotEmpty(username)){
            username = encryptor.decrypt(username);
            log.debug("Actually get username from request with: {}", username);
            if(StringUtils.isNotEmpty(username)){
                if(userRepository.findByUsername(username) == null){
                    username = null;
                }
            }
        }else{
            username = null;
        }
        return username;
    }


}
