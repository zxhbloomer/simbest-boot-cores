/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.base.model.LogicModel;
import lombok.*;

import javax.persistence.*;

/**
 * <strong>Description : 职务信息</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SysPosition extends LogicModel {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_position_seq", sequenceName = "sys_position_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_position_seq")
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String positionName; //实际职位

    @NonNull
    @Column(nullable = false)
    private Integer roleTypeId; //职位分类

    @NonNull
    @Column(nullable = false)
    private Integer orgStyleId; //组织类型

    @NonNull
    @Column(nullable = false)
    private Integer corpLevel; //职位外部等级（集团规范）

    @NonNull
    @Column(nullable = false)
    private Integer innerLevel; //职位内部等级(系统内使用)


}
