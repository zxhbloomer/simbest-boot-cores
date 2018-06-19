/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.exceptions.AttempMaxLoginFaildException;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.redis.RedisCacheUtils;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用途：重载UsernamePasswordAuthenticationFilter
 * 作者: lishuyi
 * 时间: 2018/3/7  0:10
 */
public class RsaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String LOGIN_FAILED_KEY = "LOGIN_FAILED:";

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

    /**
     * 最大错误登录次数不超过5次可尝试进行登录
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String key = LOGIN_FAILED_KEY + obtainUsername(request);
        Integer failedTimes = RedisCacheUtils.getBean(key, Integer.class);
        if(null != failedTimes && failedTimes >= AuthoritiesConstants.ATTEMPT_LOGIN_MAX_TIMES){
            throw new AttempMaxLoginFaildException("Login faild exceed max times");
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    /**
     * 登录发生错误计数，每错误一次，延时等待5分钟
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {

        String key = LOGIN_FAILED_KEY + obtainUsername(request);
        Integer failedTimes = RedisCacheUtils.getBean(key, Integer.class);
        failedTimes = null == failedTimes ? ATTEMPT_LOGIN_INIT_TIMES : failedTimes + ATTEMPT_LOGIN_INIT_TIMES;
        RedisCacheUtils.saveBean(key, failedTimes);
        RedisCacheUtils.expire(key, AuthoritiesConstants.ATTEMPT_LOGIN_FAILED_WAIT_SECONDS);

        super.unsuccessfulAuthentication(request, response, failed);
    }

//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
//            throws IOException, ServletException {
//
//        String key = LOGIN_FAILED_KEY + obtainUsername(request);
//        Long value = RedisCacheUtils.delKey(key);
//        System.out.println(value);
//
//        super.successfulAuthentication(request, response, chain, authResult);
//    }


}
