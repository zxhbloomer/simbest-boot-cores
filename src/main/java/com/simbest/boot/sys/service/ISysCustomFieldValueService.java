/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.service;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.sys.model.SysCustomFieldValue;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * 用途：实体自定义字段值逻辑层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
public interface ISysCustomFieldValueService {

    Specification<SysCustomFieldValue> getSpecification(Condition conditions);

    /**
     * 查询所有自定义字段值
     *
     * @return SysCustomField
     */
    List<SysCustomFieldValue> findAll(Specification<SysCustomFieldValue> spec);

    List<SysCustomFieldValue> saveAll(Iterable<SysCustomFieldValue> entities);
    //List<SysCustomFieldValue> save( SysCustomFieldValue  entities);

    /**
     * 查询某个自定义字段
     *
     * @param id 自定义字段Id
     * @return
     */
    Optional<SysCustomFieldValue> findById(Long id);

    void deleteById(Long id);

    SysCustomFieldValue save(SysCustomFieldValue field);

}
