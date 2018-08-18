package com.simbest.boot.sys.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.sys.model.SysDictValue;

import java.util.List;
import java.util.Map;

public interface ISysDictValueService extends ILogicService<SysDictValue,String>{

    int updateEnable(boolean enabled, String dictValueId);

    List<SysDictValue> findByParentId(String parentId);

    /**
     * 根据字典类型以及上级数据字典值id查询数据字典中相应值的name以及value的值
     */
    List<SysDictValue> findDictValue(SysDictValue sysDictValue);

    /**
     * 查看数据字典的所有值
     */
    List<Map<String,String>> findAllDictValue();
}
