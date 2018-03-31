/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 用途：系统角色类型
 * 作者: lishuyi 
 * 时间: 2018/1/31  14:39 
 */
public enum SysRoleType implements GenericEnum {

    APPLICATIONS("应用角色"), SYSTEMS("系统角色");

    @Setter @Getter
    private String value;

    SysRoleType(String value) {
        this.value = value;
    }

}
