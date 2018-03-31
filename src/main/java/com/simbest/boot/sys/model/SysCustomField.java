/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import com.simbest.boot.base.annotations.AnnotationUtils;
import com.simbest.boot.base.enums.SysCustomFieldType;
import com.simbest.boot.base.model.LogicModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

/**
 * 用途：实体自定义字段
 * 作者: lishuyi
 * 时间: 2018/1/30  17:17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SysCustomField extends LogicModel {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_custom_field_seq", sequenceName = "sys_custom_field_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_custom_field_seq")
    private Long id;

    //所属实体分类
    private String fieldClassify;

    //显示被EntityCnName注解的类的中文名称
    @Transient
    private String fieldClassifyCn;

    //字段名称
    private String fieldName;

    //字段排序值
    private Integer orderBy;

    //字段类型
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SysCustomFieldType fieldType;

    //关联数据字典，fieldType=DICT时有值
    @ManyToOne
    @JoinColumn(name = "dict_id", nullable = true)
    private SysDict sysDict;

    /**
     * @return 实体类中文名称
     */
    public String getFieldClassifyCn() {
        return AnnotationUtils.getEntityCnNameClassifyMap().get(fieldClassify);
    }
}
