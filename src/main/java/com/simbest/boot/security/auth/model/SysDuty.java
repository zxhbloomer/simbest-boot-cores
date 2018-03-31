/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.security.IDuty;
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * <strong>Description : 角色职务信息</strong><br>
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
public class SysDuty implements IDuty, GrantedAuthority {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_duty_seq", sequenceName = "sys_duty_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_duty_seq")
    private Integer id;

    @NonNull
    private String dutyCode;

    @NonNull
    private String dutyName;

    @NonNull
    private Integer dutyTypeId; //职位分类

    @NonNull
    private Integer corpLevel; //职位外部等级（集团规范）

    @NonNull
    private Integer innerLevel; //职位内部等级(系统内使用)

    @Override
    public String getAuthority() {
        return getDutyCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SysDuty rhs = (SysDuty) obj;
        return new EqualsBuilder()
                .append(getDutyCode(), rhs.getDutyCode())
                .append(getDutyTypeId(), rhs.getDutyTypeId())
                .append(getCorpLevel(), rhs.getCorpLevel())
                .append(getInnerLevel(), rhs.getInnerLevel())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDutyCode())
                .append(getDutyTypeId())
                .append(getCorpLevel())
                .append(getInnerLevel())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
