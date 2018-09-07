package com.simbest.boot.security.auth.controller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <strong>Title</strong> : 登录控制器<br>
 * <strong>Description</strong> : <br>
 * <strong>Create on</strong> : 2018/02/27<br>
 * <strong>Modify on</strong> : 2018/03/02<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@Api(description = "LoginController", tags = {"权限管理-登录校验控制器"})
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * 仅用于跳转登录页面
     * @return login
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public String login() {
        return "login";
    }


}
