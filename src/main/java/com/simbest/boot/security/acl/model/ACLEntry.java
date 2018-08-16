package com.simbest.boot.security.acl.model;

import com.simbest.boot.base.annotations.EntityIdPrefix;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 用途：表acl_entry是用于存放具体的权限信息的，其描述的就是某个主体（Sid）对某个对象（acl_object_identity）是否（granting）拥有某种权限（mask）。
 * 当同一对象acl_object_identity在acl_entry表中拥有多条记录时，就会使用ace_order来标记对应的顺序，其对应于往Acl中插入AccessControlEntry时的位置，
 * 在进行权限判断时也是依靠ace_order的顺序来进行的，ace_order越小的越先进行判断。ace是Access Control Entry的简称。
 *
 * MASK定义在org.springframework.security.acls.domain.BasePermission如下：
 * READ - 1
 * WRITE - 2
 * CREATE - 4
 * DELETE - 8
 * ADMINISTRATION - 16
 *
 * 作者: lishuyi
 * 时间: 2018/7/24  9:22
 */
@Entity
@Table(name = "acl_entry", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"acl_object_identity", "ace_order"})
})
@Data
public class ACLEntry {

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "E") //主键前缀，此为可选项注解
    private String id;

    @ManyToOne
    @JoinColumn(name = "acl_object_identity", nullable = false)
    private ACLObjectIdentity aclObjectIdentity;

    @Column(name = "ace_order", nullable = false)
    private int aceOrder;

    @ManyToOne
    @JoinColumn(name = "sid", nullable = false)
    private ACLSId aclsId;

    @Column(name = "mask", nullable = false)
    private int mask;

    @Column(name = "granting", nullable = false)
    private boolean granting;

    @Column(name = "audit_success", nullable = false)
    private boolean auditSuccess;

    @Column(name = "audit_failure", nullable = false)
    private boolean auditFailure;

    public boolean isGranting() {
        return granting;
    }

    public boolean isAuditSuccess() {
        return auditSuccess;
    }

    public boolean isAuditFailure() {
        return auditFailure;
    }

}
