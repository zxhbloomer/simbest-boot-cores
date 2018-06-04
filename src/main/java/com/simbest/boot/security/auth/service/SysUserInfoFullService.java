/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.security.auth.model.SysUserInfo;
import com.simbest.boot.security.auth.model.SysUserInfoFull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <strong>Description : 基于Spring Security的人员信息逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
public interface SysUserInfoFullService extends UserDetailsService,ILogicService<SysUserInfoFull,Integer> {

    SysUserInfo getCurrentUser();

    /**
     * 获取所有用户信息
     *
     * @return 用户信息
     */
    List<SysUserInfoFull> findAll();

    /**
     * 根据主键删除用户
     *
     * @param id 用户Id
     */
    void deleteById(Integer id);

    /**
     *
     */
    Page<SysUserInfoFull> findAll(Specification<SysUserInfoFull> conditions, PageRequest pageable);

    /**
     *
     */
    SysUserInfoFull save(SysUserInfoFull user);

    SysUserInfoFull findById(Integer id);

    /**
     * 查询某角色下面的所有人员信息
     * @param roleId     角色ID
     * @return
     */
    List<SysUserInfoFull> getUserByRole(Integer roleId);

    /**
     * 获取所属部门系统用户
     * @param orgCode
     * @return
     */
    List<SysUserInfoFull> getByOrg(Integer orgCode);

    /**
     * 根据用户名查询用户基础信息
     * @param username     用户名即OA登录用户名（英文全拼）
     * @return              用户基础信息
     */
    SysUserInfoFull findByUsername(String username);
}
