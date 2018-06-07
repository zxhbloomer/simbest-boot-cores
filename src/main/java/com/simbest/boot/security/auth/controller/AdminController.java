/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.controller;

import com.google.common.collect.Lists;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.security.IUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用途：系统管理维护控制器
 * 作者: lishuyi
 * 时间: 2018/4/25  23:49
 */
@Api(description = "系统管理维护控制器")
@Slf4j
@RestController
@RequestMapping("/sys/admin")
public class AdminController {

    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * 获取在线登录用户
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("listOnlineUsers")
    public JsonResponse listOnlineUsers() {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<String> usersNamesList = Lists.newArrayList();
        usersNamesList.addAll(principals.stream().filter(principal -> principal instanceof IUser).map(principal
                -> ((IUser) principal).getUsername()).collect(Collectors.toList()));
        return JsonResponse.success(usersNamesList);
    }
}
