/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication.sso.impl;

import com.mochasoft.portal.encrypt.EncryptorUtil;
import com.simbest.boot.security.auth.authentication.sso.SsoAuthenticationService;
import com.simbest.boot.security.auth.repository.SysUserInfoFullRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途：门户Portal单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Component
@Slf4j
public class MochaSsoAuthenticationServiceImpl implements SsoAuthenticationService {

    private final static Integer TIMEOUT = 1800;

    @Value("${app.oa.portal.token}")
    private String portalToken;

    @Autowired
    private SysUserInfoFullRepository userRepository;

    /**
     * 从请求中获取用户名
     * @param request 验证请求
     * @return 用户名
     */
    public String getUsername(HttpServletRequest request) {
        String username = null;
        try {
            log.debug("Retrive username from request with: {}", username);
            if(StringUtils.isNotEmpty(username)) {
                username = EncryptorUtil.decode(portalToken, request.getParameter("username"), TIMEOUT);
                log.debug("Actually get username from request with: {}", username);
                if (userRepository.findByUsername(username) == null) {
                    username = null;
                }
            }else{
                username = null;
            }
        } catch (Exception e) {
            log.error("EncryptorUtil parse {} with code {} failed.", request.getParameter("username"), portalToken);
        }
        return username;
    }

}