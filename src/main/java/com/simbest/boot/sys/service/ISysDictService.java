package com.simbest.boot.sys.service;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.service.IGenericService;
import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.sys.model.SysDict;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ISysDictService extends ILogicService<SysDict, Integer> {
    List<SysDict> findByParentId(Integer parentId);
    List<SysDict> findByEnabled(Boolean enabled);
    List<SysDict> findByAll();

    SysDict  findById(Integer Id);

    SysDict save(SysDict dict);

    Specification<SysDict> getSpecification(Condition conditions);


}
