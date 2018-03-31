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
 * <strong>Description : 职务类型信息</strong><br>
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
public class SysDutyType {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_duty_type_seq", sequenceName = "sys_duty_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_duty_type_seq")
    private Integer id;

    @NonNull
    private Integer orgStyleId; //即主数据规范style，自定义属性，定义组织形态。 可选值： 1. 公司 2. 部门（默认）

    @NonNull
    private String dutyType; //职务类型




}
