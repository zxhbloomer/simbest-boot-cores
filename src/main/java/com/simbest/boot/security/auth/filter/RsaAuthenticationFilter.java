/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.redis.RedisCacheUtils;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;

/**
 * 用途：重载UsernamePasswordAuthenticationFilter
 * 作者: lishuyi
 * 时间: 2018/3/7  0:10
 */
public class RsaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final Integer ATTEMPT_LOGIN_INIT_TIMES = 1;

    @Setter
    private RsaEncryptor encryptor;

    /**
     * 前端RSA加密，后端验证时，先RSA解密，再进行FormSecurityConfigurer的认证
     * @param request
     * @return
     */
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return encryptor.decrypt(request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY));
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        String username = obtainUsername(request);
        String key = "LOGIN_FAILED:" + username;
        RedisCacheUtils.seqNext(key);



        System.out.println(RedisCacheUtils.seqNext(username));
        System.out.println(RedisCacheUtils.seqNext(username));
        System.out.println(RedisCacheUtils.seqNext(username, 3));
        System.out.println(RedisCacheUtils.seqNext(username, 4));
        System.out.println(RedisCacheUtils.seqBack(username, 1));
        System.out.println(RedisCacheUtils.seqBack(username, 2));

        RedisCacheUtils.saveString(username,"2");
        System.out.println(RedisCacheUtils.getString(username));


        System.out.println(RedisCacheUtils.get(username));

        super.unsuccessfulAuthentication(request, response, failed);
    }


    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String username = obtainUsername(request);
        String key = "LOGIN_FAILED:" + username;
        RedisCacheUtils.delKey(key);

        super.successfulAuthentication(request, response, chain, authResult);
    }


}
