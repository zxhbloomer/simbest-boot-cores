package com.simbest.boot.security.auth.controller;

import com.simbest.boot.security.IUser;
import com.simbest.boot.util.security.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    @RequestMapping(value = {"/", "/sso"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String root(HttpServletRequest request) {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView index(Model indexModel) {
        IUser iuser = SecurityUtils.getCurrentUser();
        indexModel.addAttribute("iuser", iuser);
        //String json = JacksonUtils.obj2json(iuser);
        return new ModelAndView("index", "indexModel", indexModel);
    }

    /**
     * @return 403
     */
    @RequestMapping(value = "/403", method = {RequestMethod.POST, RequestMethod.GET})
    public String accesssDenied() {
        return "403";
    }

    /**
     * @return error
     */
    @RequestMapping(value = "/error", method = {RequestMethod.POST, RequestMethod.GET})
    public String error() {
        return "error";
    }
}
