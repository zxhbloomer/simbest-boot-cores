/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.authentication;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/7/27  17:48
 */
public class SortGrantedAuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

    @Override
    public int compare(GrantedAuthority o1, GrantedAuthority o2) {
        return o1.equals(o2) ? 0 : -1;
    }
}
