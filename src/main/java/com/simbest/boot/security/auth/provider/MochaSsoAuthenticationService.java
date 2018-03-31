/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途：单点登录验证服务
 * 作者: lishuyi 
 * 时间: 2018/1/20  15:06 
 */
@Component
public class MochaSsoAuthenticationService implements SsoAuthenticationService {

    @Value("${app.portal.code}")
    private String portalCode;

    /**
     * 从请求中获取用户名
     * @param request 验证请求
     * @return 用户名
     */
    public String getUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    /**
     * 根据请求验证是否为合法用户
     * @param request 验证请求
     * @param userDetails 用户信息
     * @return true/false
     */
    public Boolean authenticate(HttpServletRequest request, UserDetails userDetails) {
        return getUsername(request).equals(userDetails.getUsername());
    }

    //String username = decodePortalCode("SBKJ_RZGL_SSO", uid, 1800);
//    public static String decodePortalCode(String secretKey, String ciphertext,
//                                    int outTime) {
//        String uid = null;
//        try {
//            uid = EncryptorUtil.decode(secretKey, ciphertext, outTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return uid;
//    }
}
