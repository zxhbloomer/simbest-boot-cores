/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.simbest.boot.base.service.impl.GenericService;
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
public class SysRoleServiceImpl extends GenericService<SysRole,Integer> implements SysRoleService {

    @Autowired
    private SysRoleRepository RoleRepository;

    public List<SysRole> findAll() {
        return RoleRepository.findAll();
    }

    @Override
    public SysRole findById(Integer id) {
        return RoleRepository.findById(id).orElse(null);
    }

    @Override
    public SysRole save(SysRole role) {
        return RoleRepository.save(role);
    }


}
