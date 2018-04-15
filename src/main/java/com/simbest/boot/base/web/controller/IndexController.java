package com.simbest.boot.base.web.controller;

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
@Controller
@Slf4j
public class IndexController {

    /**
     * @return /
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public String root() {
        return "redirect:/index";
    }

    /**
     * @return index
     */
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

}
