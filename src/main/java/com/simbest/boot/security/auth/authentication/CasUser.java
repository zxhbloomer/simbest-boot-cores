/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 用途：用于传递给CAS服务器验证数据
 * 作者: lishuyi
 * 时间: 2018/8/21  16:26
 */
@Data
public class CasUser {

    @JsonProperty("id")
    private String username;

    @JsonProperty("@class")
    // 需要返回实现org.apereo.cas.authentication.principal.Principal的类名接口
    private String clazz = "org.apereo.cas.authentication.principal.SimplePrincipal";

    @JsonProperty("attributes")
    private Map<String, Object> attributes = new HashMap<>();

}
