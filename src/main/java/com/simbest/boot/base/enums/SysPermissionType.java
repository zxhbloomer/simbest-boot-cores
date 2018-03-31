/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 用途：系统权限类型
 * 作者: lishuyi 
 * 时间: 2018/1/31  14:39 
 */
public enum SysPermissionType implements GenericEnum {

    ROOT("根"),  MENU("访问路径"), BUTTON("按钮"), METHOD("执行方法");

    @Setter @Getter
    private String value;

    SysPermissionType(String value) {
        this.value = value;
    }

}
