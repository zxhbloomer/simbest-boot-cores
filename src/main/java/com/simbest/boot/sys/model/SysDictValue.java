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
//    @SequenceGenerator(name = "sys_dict_value_seq", sequenceName = "sys_dict_value_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 20)
    private String code;

    @Column(nullable = true, length = 50)
    private String name;

    @Column(nullable = true, length = 100)
    private String value;

    @Column(nullable = true, length = 11)
    private Integer orderLevel;

    @Column(nullable = true)
    private Long parentId;

    @Column(nullable = true)
    private Long dictId;

//    @OneToOne(cascade = CascadeType.ALL)
//    //根节点允许为空
//    @JoinColumn(name = "parent_id", nullable = true)
//    private SysDictValue parent;
//
//    @ManyToOne
//    @JoinColumn(name = "dict_id", nullable = false)
//    private SysDict sysDict;

}
