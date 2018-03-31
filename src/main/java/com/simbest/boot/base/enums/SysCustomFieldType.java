/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 用途：自定义字段属性类型
 * 作者: lishuyi 
 * 时间: 2018/1/31  14:39 
 */
public enum SysCustomFieldType implements GenericEnum, Comparable<SysCustomFieldType> {

    TEXT("文本"), TEXTAREA("长文本"), NUMBER("数字"), DATE("日期"), DATETIME("时间"), ZDICT("数据字典");

    @Setter @Getter
    private String value;

    SysCustomFieldType(String value) {
        this.value = value;
    }

}
