/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums;

import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.util.encrypt.Des3Encryptor;
import com.simbest.boot.util.security.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 用途：用户管理远程Api
 * 作者: lishuyi
 * 时间: 2018/4/25  23:25
 */
@Api(description = "系统用户操作相关接口")
@Slf4j
@RestController
@RequestMapping("/http/sys/user")
public class SysUserInfoHttpController {

    private final static String USER_MAPPING = "/uums/sys/user/sso/";

    @Value("${app.uums.address}")
    private String uumsAddress;

    @Autowired
    private Des3Encryptor encryptor;

    @ApiOperation(value = "查询用户信息", notes = "通过此接口来查询用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long", paramType = "query")
    @PostMapping(value = "/query")
    public JsonResponse query(@RequestParam(required = true) Long id ) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response = HttpClient.post( uumsAddress + USER_MAPPING + "query")
                .param("username", encryptor.encrypt(username))
                .param("id", String.valueOf(id))
                .asBean(JsonResponse.class);
        return response;
    }
}
