/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.github.wenhao.jpa.Specifications;
import com.simbest.boot.base.web.controller.GenericController;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.sys.model.SysLogLogin;
import com.simbest.boot.sys.service.ISysLogLoginService;
import com.simbest.boot.util.MapUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：登录日志控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Api(description = "SysLogLoginController", tags = {"系统管理-登录日志管理"})
@RestController
@RequestMapping("/sys/log/login")
public class SysLogLoginController extends GenericController<SysLogLogin, String> {

    private ISysLogLoginService service;

    @Autowired
    public SysLogLoginController(ISysLogLoginService service) {
        super(service);
        this.service = service;
    }

    @PostMapping(value = "/countLogin")
    public JsonResponse countLogin(@RequestBody SysLogLogin o) {
        return JsonResponse.success(service.countLogin(MapUtil.objectToMap(o)));
    }

    @PostMapping(value = {"/syncLoginLog", "/syncLoginLog/sso"})
    public JsonResponse sysLoginLog(@RequestBody SysLogLogin o) {
        Specification<SysLogLogin> specification = Specifications.<SysLogLogin>and()
                .between("loginTime", o.getSsDate(), o.getEeDate())
                .build();
        Iterable<SysLogLogin> datas = service.findAllNoPage(specification);
        return JsonResponse.success(datas);
    }
}
