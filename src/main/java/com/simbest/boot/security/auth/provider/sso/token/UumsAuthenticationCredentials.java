/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.provider.sso.token;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用途：基于UUMS主数据的登录认证密码凭证
 * 作者: lishuyi
 * 时间: 2018/1/20  15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UumsAuthenticationCredentials  {

    private String password;

    private String appcode;
}
