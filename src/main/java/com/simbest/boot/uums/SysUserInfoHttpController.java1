/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums;

import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.config.AppConfig;
import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.security.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private final static String USER_MAPPING = "/action/user/user/";

    @Autowired
    private AppConfig config;


    @Autowired
    private RsaEncryptor encryptor;

    @ApiOperation(value = "查询用户信息", notes = "通过此接口来查询用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/query")
    public JsonResponse query(@RequestParam Long id, @RequestParam String appcode) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {} and appcode: {}", username, appcode);
        JsonResponse response = HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findById/sso/")
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE, appcode)
                .param("id", String.valueOf(id))
                .asBean(JsonResponse.class);
        return response;
    }
}
