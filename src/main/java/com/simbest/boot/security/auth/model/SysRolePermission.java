/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.base.model.GenericModel;
import lombok.*;

import javax.persistence.*;

/**
 * <strong>Description : 角色职务权限信息</strong><br>
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
public class SysRolePermission extends GenericModel {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_role_permission_seq", sequenceName = "sys_role_permission_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_role_permission_seq")
    private Integer id;

    @NonNull
    private Integer roleId;

    @NonNull
    private Integer permissionId;

}
