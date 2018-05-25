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
    void deleteById(Long id);

    /**
     *
     */
    Page<SysUserInfoFull> findAll(Specification<SysUserInfoFull> conditions, PageRequest pageable);

    /**
     *
     */
    SysUserInfoFull save(SysUserInfoFull user);

    SysUserInfoFull findById(Long id);
}
