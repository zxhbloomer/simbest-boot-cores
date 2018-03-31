/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 用途：逻辑实体
 * 作者: lishuyi
 * 时间: 2018/1/30  21:51
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LogicModel extends SystemModel {

    @Setter
    @Getter
    @NonNull
    @Column(nullable = false)
    //是否可用
    protected Boolean enabled = true;

    @Setter
    @Getter
    @NonNull
    @Column(nullable = false)
    //是否逻辑删除
    protected Boolean removed = false;

    @Setter
    @Getter
    @NonNull
    @Column(nullable = false, updatable = false)
    //创建人 CREATOR
    protected String creator;

    @Setter
    @Getter
    @NonNull
    @Column(nullable = false)
    //更新人 MODIFIER
    protected String modifier;

}
