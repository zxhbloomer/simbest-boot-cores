/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.service.impl;


import com.simbest.boot.base.annotations.AnnotationUtils;
import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import com.simbest.boot.sys.model.SysCustomField;
import com.simbest.boot.sys.repository.SysCustomFieldRepository;
import com.simbest.boot.sys.service.ISysCustomFieldService;
import com.simbest.boot.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * 用途：实体自定义字段逻辑层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Service
public class SysCustomFieldService implements ISysCustomFieldService {

    @Autowired
    private SysCustomFieldRepository fieldRepository;

    @Autowired
    private AnnotationUtils annotationUtils;

    @Autowired
    private SysUserInfoFullService userService;

    @Override
    public Map<String, String> getFieldClassifyMap() {
        return annotationUtils.getEntityCnNameClassifyMap();
    }

    @Override
    public Specification<SysCustomField> getSpecification(Condition conditions) {
        return fieldRepository.getSpecification(conditions);
    }

    @Override
    public Page findAll(Specification<SysCustomField> conditions, PageRequest pageable) {
        return fieldRepository.findAll(conditions, pageable);
    }

    @Override
    public Optional<SysCustomField> findById(Long id) {
        return fieldRepository.findById(id);
    }

    @Override
    public SysCustomField findByFieldClassify(String fieldClassify) {
        return fieldRepository.findByFieldClassify(fieldClassify);
    }

    @Override
    public void deleteById(Long id) {
        fieldRepository.deleteById(id);
    }

    @Override
    public SysCustomField save(SysCustomField field) {
        if (field.getId() == null) {
            field.setCreator(SecurityUtils.getCurrentUserName());
        }
        field.setModifier(SecurityUtils.getCurrentUserName());
        return fieldRepository.save(field);
    }

}