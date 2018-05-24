/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.mzlion.easyokhttp.HttpClient;
import com.mzlion.easyokhttp.exception.HttpClientException;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.util.encrypt.Des3Encryptor;
import com.simbest.boot.util.json.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


/**
 * 用途：基于用户名的认证器
 * 作者: lishuyi
 * 时间: 2018/1/20  17:49
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UumsHttpValidationAuthenticationProvider implements AuthenticationProvider {

    private final static String UUMS_URL = "/uums/httpauth/validate";

    @Value("${app.uums.address}")
    private String address;

    @Autowired
    private Des3Encryptor encryptor;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        if (StringUtils.isNotEmpty(username)) {
            String jsonStr = null;
            try {
                jsonStr = HttpClient.post(address + UUMS_URL)
                        .param("username", encryptor.encrypt(username))
                        .asString();
            }catch (HttpClientException e){
                log.error("Failed to connect UUMS validation");
                Exceptions.printException(e);
                throw new
                        BadCredentialsException(username + " authenticate failed.");
            }
            JsonNode node = JacksonUtils.json2obj(jsonStr, JsonNode.class);
            if (null != node.findPath("errcode") && JsonResponse.SUCCESS_CODE == node.findPath("errcode").intValue()) {
                Collection<? extends GrantedAuthority> authorities = JacksonUtils.json2list(node.findPath("authorities")
                        .asText(ApplicationConstants.EMPTY), new TypeReference<List<GrantedAuthority>>() {
                });
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(node.findPath("principal").textValue(),
                        node.findPath("credentials").textValue(), authorities);
                return token;
            } else {
                throw new
                        BadCredentialsException(username + " authenticate failed.");
            }
        } else {
            throw new
                    BadCredentialsException(username + " authenticate failed.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
