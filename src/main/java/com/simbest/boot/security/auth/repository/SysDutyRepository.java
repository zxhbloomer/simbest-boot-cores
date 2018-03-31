/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.security.auth.model.SysDuty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <strong>Description : 基于Spring Security的职务持久层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Repository
public interface SysDutyRepository extends BaseRepository<SysDuty, Integer> {

    Set<SysDuty> findByDutyTypeId(Integer dutyTypeId);

    @Query("SELECT d FROM SysDuty d, SysUserDuty ud, SysUserInfoFull u WHERE ud.dutyId=d.id AND ud.userId=u.id AND u.id=:userId")
    Set<SysDuty> getDutyByUserId(@Param("userId") Long userId);
}
