/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用途：系统实体
 * 作者: lishuyi
 * 时间: 2018/1/30  21:49
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class SystemModel extends GenericModel {

    //创建时间
    @Setter @Getter
    @NonNull
    @Column(nullable = false, updatable = false)
    @CreationTimestamp// 创建时自动更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @Setter
    @Getter
    @NonNull
    @Column(nullable = false)
    @UpdateTimestamp// 更新时自动更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    //最后更新时间
    private LocalDateTime modifiedTime;

}
