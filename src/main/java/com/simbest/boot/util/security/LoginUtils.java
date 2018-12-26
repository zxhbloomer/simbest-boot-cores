/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.security;

import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.auth.provider.sso.token.SsoUsernameAuthentication;
import com.simbest.boot.security.auth.provider.sso.token.UsernamePrincipal;
import com.simbest.boot.util.encrypt.Md5Encryptor;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * 用途：登录工具类
 * 作者: lishuyi
 * 时间: 2018/9/21  10:43
 */
@Component
public class LoginUtils {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Md5Encryptor md5Encryptor;

    @Autowired
    private RsaEncryptor rsaEncryptor;

    @Autowired
    protected IAuthService authService;

    /**
     * 根据用户名，自动登录
     * @param username 用户名需要3DES、RSA或Mocha算法进行加密
     * @param appcode
     */
    public void manualLogin(String username, String appcode) {
        Principal principal = UsernamePrincipal.builder().username(username).build();
        SsoUsernameAuthentication authReq = new SsoUsernameAuthentication(principal, appcode);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }

    /**
     * 根据用户名和密码，自动登录
     * @param username 用户名需要RSA算法进行加密
     * @param password 密码需要由RSA加密
     * @param appcode
     */
    public void manualLogin(String username, String password, String appcode) {
        String rawUsername = rsaEncryptor.decrypt(username);
        String rawPassword = rsaEncryptor.decrypt(password);
        String md5Hex = md5Encryptor.encrypt(rawPassword);
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(rawUsername, md5Hex);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }

    /**
     * 管理员认证
     */
    public void adminLogin() {
        IUser iUser = authService.findByKey(ApplicationConstants.ADMINISTRATOR, IAuthService.KeyType.username);
        SsoUsernameAuthentication auth = new SsoUsernameAuthentication(iUser, null, iUser.getAuthorities());
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }
}
