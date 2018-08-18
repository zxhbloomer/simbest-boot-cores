package com.simbest.boot.sys.service.impl;


import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.sys.model.SysDict;
import com.simbest.boot.sys.repository.SysDictRepository;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.sys.service.ISysDictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable
    public List<SysDict> findByEnabled(Boolean enabled) {
        return dictRepository.findByEnabled(enabled);
    }


}
