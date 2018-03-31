/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.service;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.sys.model.SysCustomField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Optional;

/**
 * 用途：实体自定义字段逻辑层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
public interface ISysCustomFieldService {

    /**
     * 获取系统有自定义字段的实体类型
     *
     * @return
     */
    Map<String, String> getFieldClassifyMap();

    Specification<SysCustomField> getSpecification(Condition conditions);

    /**
     * 查询某个自定义字段
     *
     * @param id 自定义字段Id
     * @return
     */
    Optional<SysCustomField> findById(Long id);

    /**
     * 查询某个自定义字段
     *
     * @param fieldClassify 所属实体分类
     * @return
     */
    SysCustomField findByFieldClassify(String fieldClassify);

    Page findAll(Specification<SysCustomField> conditions, PageRequest pageable);

    void deleteById(Long id);

    SysCustomField save(SysCustomField field);

}
