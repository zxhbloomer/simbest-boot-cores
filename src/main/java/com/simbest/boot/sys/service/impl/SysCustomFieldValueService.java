/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.service.impl;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.sys.model.SysCustomFieldValue;
import com.simbest.boot.sys.repository.SysCustomFieldValueRepository;
import com.simbest.boot.sys.service.ISysCustomFieldValueService;
import com.simbest.boot.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用途：实体自定义字段值逻辑层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Service
public class SysCustomFieldValueService implements ISysCustomFieldValueService {

    @Autowired
    private SysCustomFieldValueRepository fieldValueRepository;

    @Override
    public Specification<SysCustomFieldValue> getSpecification(Condition conditions) {
        return fieldValueRepository.getSpecification(conditions);
    }

    @Override
    public List<SysCustomFieldValue> findAll(Specification<SysCustomFieldValue> spec) {
        return fieldValueRepository.findAll(spec);
    }

    @Override
    public List<SysCustomFieldValue> saveAll(Iterable<SysCustomFieldValue> entities) {
        return fieldValueRepository.saveAll(entities);
    }


//        @Override
//    public List<SysCustomFieldValue> saveAll( SysCustomFieldValue  entities) {
//        return fieldValueRepository.saveAll(entities);
//    }

    @Override
    public Optional<SysCustomFieldValue> findById(Long id) {
        return fieldValueRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        fieldValueRepository.deleteById(id);
    }

    @Override
    public SysCustomFieldValue save(SysCustomFieldValue field) {
        if (field.getId() == null) {
            field.setCreator(SecurityUtils.getCurrentUserName());
        }
        field.setModifier(SecurityUtils.getCurrentUserName());
        return fieldValueRepository.save(field);
    }
}
