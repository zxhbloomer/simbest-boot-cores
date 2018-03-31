/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.web.controller;

import com.simbest.boot.security.auth.repository.SysUserInfoFullRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用途：单点测试控制器
 * 作者: lishuyi
 * 时间: 2018/1/31  15:49
 */
@RestController
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private SysUserInfoFullRepository userService;


}
