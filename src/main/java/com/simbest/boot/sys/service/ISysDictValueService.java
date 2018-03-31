package com.simbest.boot.sys.service;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.sys.model.SysDictValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface ISysDictValueService {

    int updateEnableByDictId(boolean enabled, Long dictId);

    int updateEnable(boolean enabled, Long dictValueId);


    List<SysDictValue> findByDictId(Long dictId);

    Map<Long, SysDictValue> findByDictIdForKV(Long dictId);

    SysDictValue findById(Long Id);

    List<SysDictValue> findByParentId(Long parentId);

    SysDictValue save(SysDictValue dictValue);

    void deleteById(Long id);


    /**
     *
     */
    Page findAll(Pageable pageable);

    /**
     * 获取分页对象
     *
     * @param page       条件-当前页码，必须大于0，从1开始
     * @param size       条件-每页条数，必须大于0，每页最多不超过100
     * @param direction  条件-排序规则，asc/desc
     * @param properties 条件-排序属性，如：userId
     * @return 分页对象
     */
    Pageable getPageable(int page, int size, String direction, String properties);

    /**
     * 获取单表查询条件<br>
     * 只判断属性值是否相等
     *
     * @param conditions 条件
     * @return 结果集
     */
    Specification<SysDictValue> getSpecification(Condition conditions);

    /**
     *
     */
    Specification<SysDictValue> getSpecification(SysDictValue conditions);


    Page findAll(Specification<SysDictValue> conditions, Pageable pageable);
}
