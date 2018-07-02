/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import com.simbest.boot.base.model.LogicModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 用途：数据字典值
 * 作者: lishuyi
 * 时间: 2018/1/30  17:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SysDictValue extends LogicModel {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_dict_value_seq", sequenceName = "sys_dict_value_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "sys_dict_value_seq")
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String value;

    @Column
    private String description;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column
    private Integer parentId;

    @Column(nullable = false)
    private Integer dictId;

    @Transient
    private Boolean isDefault;
}
