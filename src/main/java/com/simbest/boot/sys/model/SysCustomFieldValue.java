/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import com.simbest.boot.base.model.LogicModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * 用途：实体自定义字段值
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SysCustomFieldValue extends LogicModel {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_custom_field_value_seq", sequenceName = "sys_custom_field_value_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_custom_field_value_seq")
    private Long id;

    //所属实体分类
    private String fieldClassify;

    //所属实体分类主键
    private Long fieldEntityId;

    @ManyToOne
    @JoinColumn(name = "custom_field_id", nullable = false)
    private SysCustomField customField;

    //扩展字段
    private String customFieldValue;
}
