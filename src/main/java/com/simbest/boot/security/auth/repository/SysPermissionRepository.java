/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.security.auth.model.SysPermission;
import com.simbest.boot.security.auth.model.SysRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <strong>Description : 基于Spring Security的权限持久层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Repository
public interface SysPermissionRepository extends BaseRepository<SysPermission, Integer> {

    /**
     * @param userId 用户Id
     * @return 用户基础信息
     */
    @Query(value = "SELECT DISTINCT * FROM \n" +
            "(\n" +
            "\t(SELECT  p.ID, p.DESCRIPTION AS description, p.MENU_LEVEL AS menuLevel, \n" +
            "            p.ICON AS icon, p.URL AS url, p.PARENT_ID AS parentid, p.ORDER_BY \n" +
            "            FROM sys_user_info_full u,sys_role r,sys_user_role ur,sys_permission p,sys_role_permission rp \n" +
            "            WHERE ur.username=u.username AND ur.role_id=r.id AND rp.role_id=r.id AND rp.permission_id=p.id \n" +
            "            AND u.username=:username AND p.PERMISSION_TYPE='MENU')\n" +
            "UNION\n" +
            "\t(SELECT  p.ID, p.DESCRIPTION AS description, p.MENU_LEVEL AS menuLevel, \n" +
            "            p.ICON AS icon, p.URL AS url, p.PARENT_ID AS parentid, p.ORDER_BY \n" +
            "            FROM sys_user_info_full u,sys_permission p,sys_user_permission up \n" +
            "            WHERE up.username=u.username AND up.permission_id=p.id \n" +
            "            AND u.username=:username AND p.PERMISSION_TYPE='MENU')\n" +
            ") \n" +
            "AS tbl ORDER BY ORDER_BY ASC", nativeQuery = true)
    List<Map<String, Object>> findMenu(@Param("username") String username);

    @Query(value = "SELECT DISTINCT * FROM \n" +
            "(\n" +
            "\t(SELECT  p.*" +
            "            FROM sys_user_info_full u,sys_role r,sys_user_role ur,sys_permission p,sys_duty_permission dp \n" +
            "            WHERE ur.user_id=u.id AND ur.role_id=r.id AND rp.role_id=r.id AND rp.permission_id=p.id \n" +
            "            AND u.username=:username AND p.PERMISSION_TYPE='MENU')\n" +
            "UNION\n" +
            "\t(SELECT  p.*" +
            "            FROM sys_user_info_full u,sys_permission p,sys_user_permission up \n" +
            "            WHERE up.user_id=u.id AND up.permission_id=p.id \n" +
            "            AND u.username=:username AND p.PERMISSION_TYPE='MENU')\n" +
            ") \n" +
            "AS tbl ORDER BY ORDER_BY ASC", nativeQuery = true)
    Set<SysPermission> getPermissionByUsername(@Param("username") String username);

    @Query(value = "SELECT p FROM SysPermission p, SysUserPermission up, SysUserInfoFull u WHERE up.username=u.username AND up.permissionId=p.id AND u.username=:username")
    Set<SysPermission> getSpecailPermissionyUsername(@Param("username") String username);

    @Query(value = "SELECT p FROM SysPermission p, SysRolePermission rp, SysRole r WHERE rp.roleId=r.id AND rp.permissionId=p.id AND r.id=:roleId")
    Set<SysPermission> getPermissionByRoleId(@Param("roleId") Integer roleId);


}
