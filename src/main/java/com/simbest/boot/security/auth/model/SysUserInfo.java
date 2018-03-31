/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.security.IUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * <strong>Description : 满足《用户主数据管理规范V1.0》和微信互联网应用的用户信息</strong><br>
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
public class SysUserInfo implements IUser, UserDetails {

    @NonNull
    @Column(nullable = false)
    //是否可用
    protected Boolean enabled = true;
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_user_info_full_seq", sequenceName = "sys_user_info_full_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_user_info_full_seq")
    private Long id;
    @NonNull
    @Column(unique = true)
    private String username; //即主数据规范uid，引用自inetOrgPerson，定义用户登录ID，参见附录内部用户命名规范和外部用户命名规范。
    private String password; //用户密码
    @NonNull
    private String truename; //即主数据规范cn，定义用户中文姓名。
    private String nickname;
    private String displayOrder; //定义用户显示顺序
    @NonNull
    private String orgCode; //即主数据规范o，定义用户所属的组织的编码号，编码定义参见附录中的组织编码规则。例如：“00010002000300040005”
    private String duty; //定义用户的职务编码，引用“职务”对象。例如：“GD00000001”。
    private String mobile; //定义用户移动电话
    @NonNull
    private String status; //定义用户帐号的状态，可选值参见附录用户帐号状态的数据字典定义。例如：“0”表示正常状态。
    private String photo; //照片
    private String openid; //微信openid
    private String unionid; //微信unionid
    private String reserve1; //预留扩展字段１
    private String reserve2; //预留扩展字段2
    private String reserve3; //预留扩展字段3
    private String reserve4; //预留扩展字段4
    private String reserve5; //预留扩展字段5
    @NonNull
    @Column(nullable = false)
    //账户是否过期
    private Boolean accountNonExpired;
    @NonNull
    @Column(nullable = false)
    //账户是否锁定
    private Boolean accountNonLocked;
    @NonNull
    @Column(nullable = false)
    //密码是否过期
    private Boolean credentialsNonExpired;
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Transient
    private Collection<SysDuty> authDutys;

    @Transient
    private Collection<SysPermission> authPermissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> sortedAuthorities = new TreeSet<>();
        sortedAuthorities.addAll(authDutys);
        sortedAuthorities.addAll(authPermissions);
        return sortedAuthorities;
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
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
        SysUserInfo rhs = (SysUserInfo) obj;
        return new EqualsBuilder()
                .append(getUsername(), rhs.getUsername())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getUsername())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

}
