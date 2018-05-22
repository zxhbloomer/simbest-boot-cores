/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.GenericRepository;
import com.simbest.boot.security.auth.model.SysRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
public interface SysRoleRepository extends GenericRepository<SysRole, Integer> {

    @Query("SELECT r FROM SysRole r, SysUserRole ur, SysUserInfoFull u WHERE ur.roleId=r.id AND ur.username=u.username AND u.username=:username")
    Set<SysRole> getRoleByUsername(@Param("username") String username);
}
