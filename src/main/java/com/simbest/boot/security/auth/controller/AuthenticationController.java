package com.simbest.boot.security.auth.controller;


import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：登录校验控制器
 * 作者: lishuyi
 * 时间: 2018/1/31  15:49
 */
@Api(description = "登录校验控制器")
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private SysUserInfoFullService sysUserInfoService;

    @PostMapping("/validate")
    public JsonResponse validate(@RequestParam String username) {
        UserDetails userDetails = sysUserInfoService.loadUserByUsername(username);
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    userDetails.getPassword(), userDetails.getAuthorities());
            return JsonResponse.success(token);
        } else {
            return JsonResponse.defaultErrorResponse();
        }
    }
}
