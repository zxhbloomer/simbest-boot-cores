/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import com.simbest.boot.util.json.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * 用途：基于用户名的认证器
 * 作者: lishuyi
 * 时间: 2018/1/20  17:49
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpRemoteUsernameAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserInfoFullService sysUserInfoService;

    @Value("${security.auth.validate.url}")
    private String validateUrl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String jsonStr = HttpClient.post(validateUrl)
                .param("username",username)
                .asString();
        JsonNode node = JacksonUtils.json2obj(jsonStr, JsonNode.class);
        if (null != node.findPath("errcode") && JsonResponse.SUCCESS_CODE == node.findPath("errcode").intValue()) {
            return new UsernamePasswordAuthenticationToken(node.findPath("principal"), node.findPath("credentials"));
        } else {
            throw new
                    UsernameNotFoundException(username + " is not exist account.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
