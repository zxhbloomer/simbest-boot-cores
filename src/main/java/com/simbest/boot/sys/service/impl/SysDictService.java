package com.simbest.boot.sys.service.impl;


import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import com.simbest.boot.sys.model.SysDict;
import com.simbest.boot.sys.repository.SysDictRepository;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.sys.service.ISysDictValueService;
import com.simbest.boot.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictService implements ISysDictService {

    @Autowired
    private SysDictRepository dictRepository;

    @Autowired
    private ISysDictValueService dictValueService;

    @Autowired
    private SysUserInfoFullService userService;

    @Override
    public int updateEnableByIds(Boolean enabled, List<Long> ids) {
        for (Long dictId : ids) {
            SysDict dict = findById(dictId);
            if (dict == null) {
                continue;
            }
            dict.setEnabled(enabled);
            dictRepository.save(dict);
            dictValueService.updateEnableByDictId(enabled, dictId);
            List<SysDict> list = findByParentId(dictId);
            if (list.size() > 0) {
                updateEnable(enabled, list);
            }
        }
        return 1;
    }

    public int updateEnable(boolean enabled, List<SysDict> dicts) {
        int ret = 0;
        for (SysDict dict : dicts) {
            dict.setEnabled(enabled);
            dictRepository.save(dict);
            dictValueService.updateEnableByDictId(enabled, dict.getId());
            List<SysDict> list = findByParentId(dict.getId());
            if (list.size() > 0) {
                updateEnable(enabled, list);
            }
        }
        return ret;
    }

    @Override
    public List<SysDict> findByParentId(Long parentId) {
        return dictRepository.findByParentId(parentId);
    }

    @Override
    public SysDict findById(Long id) {
        return dictRepository.findById(id).orElse(null);
    }

    @Override
    public List<SysDict> findByAll() {
        return dictRepository.findAll();
    }

    @Override
    public List<SysDict> findByEnabled(Boolean enabled) {
        return dictRepository.findByEnabled(enabled);
    }

    @Override
    public SysDict save(SysDict dict) {
        if (dict.getId() == null) {
            dict.setCreator(SecurityUtils.getCurrentUserName());
        }
        dict.setModifier(SecurityUtils.getCurrentUserName());
        return dictRepository.save(dict);
    }

    @Override
    public Specification<SysDict> getSpecification(Condition conditions) {
        return dictRepository.getSpecification(conditions);
    }

}
