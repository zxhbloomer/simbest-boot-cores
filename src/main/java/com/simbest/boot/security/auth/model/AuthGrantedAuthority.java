/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/4/26  19:37
 */
public class AuthGrantedAuthority implements GrantedAuthority {

    @Setter
    private String privilege;

    @Override
    public String getAuthority() {
        return this.privilege;
    }
}
