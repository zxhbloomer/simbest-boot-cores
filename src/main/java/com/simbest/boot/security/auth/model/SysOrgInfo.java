/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.security.IOrg;
import lombok.*;

import javax.persistence.*;

/**
 * <strong>Description : 满足《用户主数据管理规范V1.0》和微信互联网应用的组织信息</strong><br>
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
@MappedSuperclass
public class SysOrgInfo extends LogicModel implements IOrg {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_org_info_full_seq", sequenceName = "sys_org_info_full_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_org_info_full_seq")
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String orgName; //即主数据规范ou，定义组织的简称，可以作为组织的显示名。例如：“网管中心”

    @NonNull
    @Column(nullable = false)
    private String orgCode; //即主数据规范o，为组织的编码，编码规则参考附录中的组织编码规范。 例如：某组织编码为00010002 000000000000

    @NonNull
    @Column(nullable = false)
    private String displayName; //定义组织名称，用于显示。 例如：某组织显示名称为“咸阳网络部-网络优化班”

    @NonNull
    @Column(nullable = false)
    private Integer orgStyleId; //即主数据规范style，自定义属性，定义组织形态。 可选值： 1. 公司 2. 部门（默认）

    @NonNull
    @Column(nullable = false)
    private Integer status; //定义组织的状态，默认值为0，例如：“0”表示正常状态，“1”表示锁定状态。

    @NonNull
    private Integer parentOrgId; //上级组织编码

    @NonNull
    @Column(nullable = false)
    private Integer orgLevelId; //组织级别,即组织级别orgLevel

    private String displayOrder; //同一个父节点下的组织的显示顺序

    private String reserve1; //预留扩展字段1

    private String reserve2; //预留扩展字段2

    private String reserve3; //预留扩展字段3

    private String reserve4; //预留扩展字段4

    private String reserve5; //预留扩展字段5

}
