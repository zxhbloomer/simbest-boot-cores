package com.simbest.boot.sys.service.impl;


import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.sys.model.SysDict;
import com.simbest.boot.sys.repository.SysDictRepository;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.sys.service.ISysDictValueService;
import com.simbest.boot.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictService extends LogicService<SysDict, String> implements ISysDictService {

    private SysDictRepository dictRepository;

    @Autowired
    private ISysDictValueService dictValueService;

    @Autowired
    public SysDictService(SysDictRepository dictRepository ) {
        super(dictRepository);
        this.dictRepository = dictRepository;
    }

    @Override
    public List<SysDict> findByParentId(String parentId) {
        return dictRepository.findByParentId(parentId);
    }

    @Override
    public SysDict findById(String id) {
        return dictRepository.findById(id).orElse(null);
    }

    @Override
    public List<SysDict> findByAll() {
        return dictRepository.findAll();
    }

    @Override
    @Cacheable
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
