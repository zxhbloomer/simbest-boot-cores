package com.simbest.boot.sys.service;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.service.IGenericService;
import com.simbest.boot.sys.model.SysDict;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ISysDictService extends IGenericService<SysDict,Long>{

    int updateEnableByIds(Boolean enabled, List<Long> ids);

    List<SysDict> findByParentId(Long parentId);
    List<SysDict> findByEnabled(Boolean enabled);
    List<SysDict> findByAll();

    SysDict  findById(Long Id);

    SysDict save(SysDict dict);

    Specification<SysDict> getSpecification(Condition conditions);


}
