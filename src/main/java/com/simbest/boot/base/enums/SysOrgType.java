/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 用途：系统组织类型
 * 作者: lishuyi 
 * 时间: 2018/1/31  14:39 
 */
public enum SysOrgType implements GenericEnum {

    ROOT("根"),  GROUP("一级组织_集团级"), CORP("二级组织_公司级"), DEPT("三级组织_部门级"), OFFICE("四级组织_科室级");

    @Setter @Getter
    private String value;

    SysOrgType(String value) {
        this.value = value;
    }

}
