/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.security.auth.model.SysPermission;
import com.simbest.boot.security.auth.repository.SysPermissionRepository;
import com.simbest.boot.security.auth.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <strong>Description : 基于Spring Security的权限逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Service
public class SysPermissionServiceImpl extends LogicService<SysPermission,Integer> implements SysPermissionService {

    @Autowired
    private SysPermissionRepository permissionRepository;

    public SysPermissionServiceImpl ( LogicRepository<SysPermission, Integer> logicRepository ) {
        super( logicRepository );
    }

    @Override
    public List<Map<String, Object>> findMenu(String username) {
        return permissionRepository.findMenu(username);
    }

    @Override
    public List<SysPermission> findAll(){
        return permissionRepository.findAll();
    }

}
