/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.security.auth.model.SysUserInfoFull;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <strong>Description : 基于Spring Security的用户持久层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Repository
public interface SysUserInfoFullRepository extends LogicRepository<SysUserInfoFull, Integer> {

    /**
     * @param username 登录标识
     * @return 用户基础信息
     */
    SysUserInfoFull findByUsername(String username);

    /**
     * @param username 登录标识
     * @return 用户基础信息列表
     */
    List<SysUserInfoFull> findByUsernameStartingWith(String username);

    /**
     * @param username 登录标识
     * @return 用户基础信息列表
     */
    List<SysUserInfoFull> findByUsernameContaining(String username);

    /**
     * 查询某角色下面的所有人员信息
     * @param roleId     角色ID
     * @return
     */
    @Query (value = "SELECT u.*,o.id as orgId,o.org_name,o.org_code,o.org_level_id from SYS_USER_INFO_FULL u, sys_user_role ur,SYS_ORG_INFO_FULL o WHERE u.removed=0 AND u.username=ur.username and o.id=u.org_code and ur.role_id = ?1 ORDER BY NVL(u.level_id,0) ASC, u.level_id ASC, NVL(u.display_order,0) ASC, u.display_order ASC, u.username ASC",nativeQuery = true)
    List<SysUserInfoFull> getUserByRole(Integer roleId);

    /**
     * 获取所属部门系统用户
     * @param orgCode
     * @return
     */
    String sql1 = "select u.*,o.id as orgId,o.org_name,o.org_code,o.org_level_id from SYS_USER_INFO_FULL u,SYS_ORG_INFO_FULL o where u.enabled=1 AND u.removed=0 AND u.org_code=o.id " +
            "and o.id = :orgCode ORDER BY NVL(u.level_id,0) ASC, u.level_id ASC, NVL(u.display_order,0) ASC, u.display_order ASC, username ASC";
    @Query(value = sql1,nativeQuery = true)
    List<SysUserInfoFull> getByOrg(@Param ("orgCode")Integer orgCode);
}
