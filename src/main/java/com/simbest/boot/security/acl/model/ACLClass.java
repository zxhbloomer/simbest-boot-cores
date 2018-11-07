package com.simbest.boot.security.acl.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用途：用来保存对象类型的，字段class中保存的是对应对象的全限定名。Acl需要使用它来区分不同的对象类型。
 * 作者: lishuyi
 * 时间: 2018/7/24  9:22
 */
@Entity
@Table(name = "acl_class")
@Data
public class ACLClass implements Serializable {

    @Id
    @Column(name = "id", length = 40)
    @SequenceGenerator(name = "ACL_CLASS_SEQ", sequenceName = "ACL_CLASS_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ACL_CLASS_SEQ")
    private String id;

    @Column(name = "class", nullable = false, unique = true)
    private String clazz;
}
