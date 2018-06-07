/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;


import com.google.common.collect.Lists;
import com.simbest.boot.base.model.LogicModel;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    @SequenceGenerator (name = "sys_dict_seq", sequenceName = "sys_dict_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "sys_dict_seq")
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    private Integer displayOrder;

    private Integer parentId;

    @OneToMany(mappedBy="sysDict", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SysDictValue> values = Lists.newArrayList();
}
