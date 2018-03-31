/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.simbest.boot.security.auth.model.SysDuty;
import com.simbest.boot.security.auth.repository.SysDutyRepository;
import com.simbest.boot.security.auth.service.SysDutyService;
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
public class SysDutyServiceImpl implements SysDutyService {

    @Autowired
    private SysDutyRepository RoleRepository;

    public List<SysDuty> findAll() {
        return RoleRepository.findAll();
    }

    @Override
    public SysDuty findById(Integer id) {
        return RoleRepository.findById(id).orElse(null);
    }

    @Override
    public SysDuty save(SysDuty role) {
        return RoleRepository.save(role);
    }


}
