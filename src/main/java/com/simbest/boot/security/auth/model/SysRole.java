/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.security.IRole;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * <strong>Description : 角色信息</strong><br>
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
public class SysRole extends LogicModel implements IRole, GrantedAuthority {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_role_seq", sequenceName = "sys_role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_role_seq")
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String roleCode; //系统角色编码

    @NonNull
    @Column(nullable = false)
    private String roleName; //角色名称

    @NonNull
    @Column(nullable = false)
    private Integer orgStyleId; //组织类型


    @Override
    public String getAuthority() {
        return getRoleCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SysRole rhs = (SysRole) obj;
        return new EqualsBuilder()
                .append(getRoleCode(), rhs.getRoleCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getRoleCode())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
