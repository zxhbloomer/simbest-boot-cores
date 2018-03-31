/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * <strong>Description : 组织用户信息</strong><br>
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
public class SysOrgUserInfo {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_org_user_info_seq", sequenceName = "sys_org_user_info_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_org_user_info_seq")
    private Integer id;
    
    @NonNull
    private String	orgCode	; //	定义用户所属的组织的编码号，编码定义参见附录中的组织编码规则。例如：“00010002000300040005”

    @NonNull
    private String	username 	; //	引用自inetOrgPerson，定义用户登录ID，参见附录内部用户命名规范和外部用户命名规范。

    private String	displayOrder	; //	定义用户显示顺序

    private String	duty	; //	定义用户的职务编码，引用“职务”对象。例如：“GD00000001”。

    @NonNull
    private String	status	; //	定义用户帐号的状态，可选值参见附录用户帐号状态的数据字典定义。例如：“0”表示正常状态。

}
