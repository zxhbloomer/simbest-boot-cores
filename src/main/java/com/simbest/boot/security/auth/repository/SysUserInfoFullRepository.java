/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.security.auth.model.SysUserInfo;
import com.simbest.boot.security.auth.model.SysUserInfoFull;
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
public interface SysUserInfoFullRepository extends BaseRepository<SysUserInfoFull, Long> {

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

}
