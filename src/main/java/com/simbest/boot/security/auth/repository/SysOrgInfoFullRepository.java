package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.security.auth.model.SysOrgInfoFull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * <strong>Description : 基于Spring Security的组织持久层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
public interface SysOrgInfoFullRepository extends LogicRepository<SysOrgInfoFull, Integer> {

    String sql1 = " SELECT t.id FROM sys_org_info_full t WHERE t.parentOrgId = :id " ;
    @Query (value = sql1, nativeQuery = true)
    List<Integer> getByParentId ( @Param ( "id" ) int id );

    List<SysOrgInfoFull> findByOrgCodeLikeAndStatus(String orgCode, Boolean status);

    List<SysOrgInfoFull> findByParentOrgIdAndStatus(Integer parentOrgId, Boolean status);

    @Query("SELECT o FROM SysOrgInfoFull o, SysOrgUserInfo ou, SysUserInfoFull u WHERE ou.orgCode=o.orgCode AND ou.username=u.username AND u.username=:username " +
            "ORDER BY o.orgLevelId ASC, ou.displayOrder ASC")
    Set<SysOrgInfoFull> getOrgByUsername(@Param("username") String username);

    /**
     * 获取根组织
     * @return
     */
    @Query(value = "SELECT * FROM sys_org_info_full org WHERE parentOrgId is null",nativeQuery = true)
    SysOrgInfoFull getRoot();

    /**
     * 通过父亲部门获取部门
     * @param parentId
     * @return
     */
    String sql2 = "SELECT c.*,p.id AS pid,p.org_name AS porgName,p.org_code AS porgCode,p.org_level_id AS porgLevel FROM SYS_ORG_INFO_FULL c LEFT JOIN SYS_ORG_INFO_FULL p ON c.parent_org_id=p.id " +
            "WHERE c.parent_org_id = :parentId and c.removed=0 ORDER BY NVL(c.display_order,0) ASC, c.display_order ASC";
    @Query(value = sql2,nativeQuery = true)
    List<SysOrgInfoFull> getByParent(@Param("parentId") Integer parentId);


    String sql3 = "SELECT * FROM sys_org_info_full t WHERE t.org_code = ?1";
    @Query(value = sql3,nativeQuery = true)
    SysOrgInfoFull getByOrgCode(String orgCode);
}
