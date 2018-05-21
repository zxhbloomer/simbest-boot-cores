/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;


import com.simbest.boot.base.enums.SysDictType;
import com.simbest.boot.base.model.LogicModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 用途：数据字典
 * 作者: lishuyi
 * 时间: 2018/1/30  17:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SysDict extends LogicModel {

    @Id
    @Column(name = "id")
    @SequenceGenerator (name = "sys_dict_seq", sequenceName = "sys_dict_seq")
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "sys_dict_seq")
    private Long id;

    @Column(nullable = true, length = 20)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SysDictType dictType;

    @Column(nullable = true, length = 100)
    private String description;

    @Column(nullable = true, length = 11)
    private Integer orderLevel;

//    @OneToOne(cascade = CascadeType.ALL)
//    //根节点允许为空
//    @JoinColumn(name = "parent_id", nullable = true)
//    private SysDict parent;

    @Column(nullable = true)
    private Long parentId;
}
