package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.security.auth.model.SysOrgInfoFull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <strong>Description : 基于Spring Security的组织持久层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
public interface SysOrgInfoFullRepository extends BaseRepository<SysOrgInfoFull, Integer> {

    String sql1 = " SELECT t.id FROM sys_org_info_full t WHERE t.parentOrgId = :id " ;
    @Query (value = sql1, nativeQuery = true)
    List<Integer> getByParentId ( @Param ( "id" ) int id );

    List<SysOrgInfoFull> findByOuLikeAndStatus ( String ou, Boolean status);

    List<SysOrgInfoFull> findByParentOrgIdAndStatus(Integer parentOrgId, Boolean status);

}
