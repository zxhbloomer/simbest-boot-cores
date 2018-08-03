/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider;

import com.mzlion.easyokhttp.HttpClient;
import com.mzlion.easyokhttp.exception.HttpClientException;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.constants.ErrorCodeConstants;
import com.simbest.boot.exceptions.AccesssAppDeniedException;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.SimpleUser;
import com.simbest.boot.security.auth.authentication.token.UumsAuthentication;
import com.simbest.boot.security.auth.authentication.token.UumsAuthenticationCredentials;
import com.simbest.boot.util.json.JacksonUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


/**
 * 用途：基于用户名的认证器
 * 作者: lishuyi
 * 时间: 2018/1/20  17:49
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UumsHttpValidationAuthenticationProvider implements AuthenticationProvider {

    private final static String UUMS_URL = "/httpauth/validate";

    @Value("${app.uums.address}")
    private String address;

    @Setter
    @Getter
    protected boolean hideUserNotFoundExceptions = false;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        if (null != principal && credentials != null) {
            IUser authUser = null;
            try {
                UumsAuthenticationCredentials uumsCredentials = (UumsAuthenticationCredentials)credentials;
                JsonResponse response = HttpClient.post(address + UUMS_URL)
                        .param(AuthoritiesConstants.SSO_UUMS_USERNAME, principal.toString())
                        .param(AuthoritiesConstants.SSO_UUMS_PASSWORD, uumsCredentials.getPassword())
                        .param(AuthoritiesConstants.SSO_API_APP_CODE, uumsCredentials.getAppcode())
                        .asBean(JsonResponse.class);
                if(response.getErrcode().equals(ErrorCodeConstants.ERRORCODE_LOGIN_APP_UNREGISTER_GROUP)){
                    log.error("User {} try to login {} failed, because uums return error with: {}", principal, uumsCredentials.getAppcode(), ErrorCodeConstants.LOGIN_APP_UNREGISTER_GROUP);
                    throw new
                            AccesssAppDeniedException(ErrorCodeConstants.LOGIN_APP_UNREGISTER_GROUP);
                }else {
                    String userJson = JacksonUtils.obj2json(response.getData());
                    authUser = JacksonUtils.json2obj(userJson, SimpleUser.class);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser,
                            authUser.getPassword(), authUser.getAuthorities());
                    return token;
                }
            }catch (HttpClientException e){
                log.error("User {} try to login failed, because catch a http exception!", principal);
                throw new
                        BadCredentialsException(principal + " authenticate failed.");
            }catch (Exception e){
                log.error("User {} try to login failed, because catch an unknow exception!", principal);
                throw new
                        BadCredentialsException(principal + " authenticate failed.");
            }
        } else {
            log.error("User {} login with {} failed, because principal or credentials is null!", principal, credentials);
            throw new
                    BadCredentialsException(principal + " authenticate failed.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UumsAuthentication.class);
    }
}
