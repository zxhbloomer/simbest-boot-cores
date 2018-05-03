package com.simbest.boot.security.auth.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用途：首页控制器
 * 作者: lishuyi
 * 时间: 2018/1/31  15:49
 */
@Api(description = "登录控制器")
@Slf4j
@Controller
public class IndexController {

    /**
     * @return /
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public String root() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index", method = {RequestMethod.POST, RequestMethod.GET})
    public String index() {
        return "index";
    }

    /**
     * @return 403
     */
    @RequestMapping(value = "/403", method = {RequestMethod.POST, RequestMethod.GET})
    public String accesssDenied() {
        return "403";
    }

    @RequestMapping(value = "/b", method = {RequestMethod.POST, RequestMethod.GET})
    public String b() {
        return "b";
    }
}
