/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.security.auth.model.SysRole;
import com.simbest.boot.security.auth.repository.SysRoleRepository;
import com.simbest.boot.security.auth.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <strong>Description : 基于Spring Security的职务逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Service
public class SysRoleServiceImpl extends LogicService<SysRole,Integer> implements SysRoleService {


    private SysRoleRepository roleRepository;

    @Autowired
    public SysRoleServiceImpl ( SysRoleRepository sysRoleRepository ) {
        super( sysRoleRepository );
        this.roleRepository = sysRoleRepository;
    }

    public List<SysRole> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public SysRole findById(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public SysRole save(SysRole role) {
        return roleRepository.save(role);
    }

    /**
     * 根据用户名查询所属角色列表
     * @param username  用户名即OA登录用户名（英文全拼）
     * @return
     */
    @Override
    public List<SysRole> getByUser ( String username ) {
        return ( List<SysRole> ) roleRepository.getRoleByUsername( username );
    }
}
