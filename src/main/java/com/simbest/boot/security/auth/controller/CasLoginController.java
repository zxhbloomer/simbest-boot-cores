/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/8/20  17:55
 */
@Controller
public class CasLoginController {

    /**
     * 重定向从CAS登录成功后返回的登陆页为首页
     * @return
     */
    @GetMapping("/caslogin")
    public String login() {
        return "redirect:/index";
    }

}
