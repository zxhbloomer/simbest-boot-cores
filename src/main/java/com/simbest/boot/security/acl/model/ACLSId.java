package com.simbest.boot.security.acl.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;


/**
 * 用途：用来保存Sid的。有两种类型的Sid，一种是基于用户的Sid，叫PrincipalSid；
 *      另一种是基于GrantedAuthority的Sid，叫GrantedAuthoritySid。
 *      acl_sid表的sid字段存放的是用户名或者是GrantedAuthority的字符串表示。
 * 作者: lishuyi
 * 时间: 2018/7/24  9:22
 */
@Entity
@Table(name = "acl_sid", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sid", "principal"})
})
@Data
public class ACLSId implements Serializable {

    @Id
    @Column(name = "id", length = 40)
    @SequenceGenerator(name = "ACL_SID_SEQ", sequenceName = "ACL_SID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ACL_SID_SEQ")
    private String id;

    @Column(name = "principal", nullable = false)
    private boolean principal;

    @Column(name = "sid", nullable = false)
    private String sId;

    public boolean isPrincipal() {
        return principal;
    }

}
