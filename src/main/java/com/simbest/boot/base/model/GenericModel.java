/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 用途：通用实体
 * 作者: lishuyi
 * 时间: 2018/1/30  21:47
 */
@Setter
@Getter
@MappedSuperclass
public abstract class GenericModel implements Serializable {

    @Transient
    //动态排序
    private String orderByClause;

    @Transient
    //时间区间-开始时间
    private Date ssDate;

    @Transient
    //时间区间-结束时间
    private Date eeDate;

    @Transient
    //分页起始页码
    private Integer pageIndex;

    @Transient
    //分页每页记录数
    private Integer pagesize;
}
