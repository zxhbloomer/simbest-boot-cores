/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.base.enums.SysPermissionType;
import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.security.IPermission;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * <strong>Description : 权限信息</strong><br>
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
public class SysPermission extends LogicModel implements IPermission, GrantedAuthority {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_permission_seq", sequenceName = "sys_permission_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_permission_seq")
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String permissionCode;

    //描述
    private String description;

    //路径
    private String url;

    //图标
    private String icon;

    //菜单级别
    private Integer menuLevel;

    @Column(nullable = false)
    private Integer orderBy;

    @NonNull
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SysPermissionType permissionType;

    @Column(nullable = true)
    private Integer parentId;

    @Column(length = 4000, nullable = true)
    private String remark;

    @Override
    public String getAuthority() {
        return getPermissionCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SysPermission rhs = (SysPermission) obj;
        return new EqualsBuilder()
                .append(getPermissionCode(), rhs.getPermissionCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getPermissionCode())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public String getType() {
        return getPermissionType().getValue();
    }
}
