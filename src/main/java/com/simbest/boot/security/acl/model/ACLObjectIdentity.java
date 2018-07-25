package com.simbest.boot.security.acl.model;

import lombok.Data;

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
import java.io.Serializable;

/**
 * 用途：用来存放需要进行访问控制的对象的信息的。其保存的信息有对象的拥有者、对象的类型、对象的主键、对象的父对象和是否继承父对象的权限。
 * 作者: lishuyi
 * 时间: 2018/7/24  9:22
 */
@Entity
@Table(name = "acl_object_identity", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"object_id_class", "object_id_identity"})
})
@Data
public class ACLObjectIdentity implements Serializable {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "acl_object_identity_seq", sequenceName = "acl_object_identity_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "acl_object_identity_seq")
    private Long id;

    //关联acl_class，表示对象类型
    @ManyToOne
    @JoinColumn(name = "object_id_class", nullable = false)
    private ACLClass aclClass;

    //对象的主键
    @Column(name = "object_id_identity", nullable = false)
    private String objectIdIdentity;

    //父对象的id，关联acl_object_identity
    @ManyToOne
    @JoinColumn(name = "parent_object")
    private ACLObjectIdentity parentObject;

    //拥有者的sid，关联acl_sid
    @ManyToOne
    @JoinColumn(name = "owner_sid")
    private ACLSId ownerSId;

    //是否继承父对象的权限。
    //例如：删除对象childObj需要有delete权限，用户A没有childObj的delete权限，但是有childObj的父对象parentObj的delete权限，
    // 当entries_inheriting为true时，用户A同样可以删除childObj。
    @Column(name = "entries_inheriting", nullable = false)
    private boolean entriesInheriting;



    public boolean isEntriesInheriting() {
        return entriesInheriting;
    }

}
