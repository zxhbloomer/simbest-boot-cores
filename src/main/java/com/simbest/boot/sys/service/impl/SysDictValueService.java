package com.simbest.boot.sys.service.impl;

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.sys.model.SysDictValue;
import com.simbest.boot.sys.repository.SysDictValueRepository;
import com.simbest.boot.sys.service.ISysDictValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysDictValueService extends LogicService<SysDictValue,String> implements ISysDictValueService{

    private SysDictValueRepository dictValueRepository;

    @Autowired
    public SysDictValueService(SysDictValueRepository dictValueRepository) {
        super(dictValueRepository);
        this.dictValueRepository = dictValueRepository;
    }

    @Override
    public int updateEnable(boolean enabled, String dictValueId) {
        SysDictValue val = findById(dictValueId);
        if (val == null) {
            return 0;
        }
        val.setEnabled(enabled);
        this.update(val);
        List<SysDictValue> list = findByParentId(dictValueId);
        for (SysDictValue v : list) {
            updateEnable(enabled, v.getId());
        }
        return 1;
    }

    @Override
    public List<SysDictValue> findByParentId(String parentId) {
        return dictValueRepository.findByParentId(parentId);
    }

    @Override
    public SysDictValue findById(String id) {
        return dictValueRepository.findById(id).orElse(null);
    }

    /**
     * 根据字典类型以及上级数据字典值id查询数据字典中相应值的name以及value的值
     * @param sysDictValue
     * @return
     */
    @Override
    public List<SysDictValue> findDictValue (SysDictValue sysDictValue) {
        if(sysDictValue==null){
            log.error( "--传来的参数不正确！--" );
            return null;
        }
        List<SysDictValue> sysDictValueList=new ArrayList<>(  );
        if(sysDictValue.getParentId()!=null){
            sysDictValueList=dictValueRepository.findDictValue(sysDictValue.getDictType(),sysDictValue.getParentId());
            sysDictValueList.get(0).setIsDefault(true);
            return sysDictValueList;
        }
        sysDictValueList=dictValueRepository.findDictValue(sysDictValue.getDictType());
        sysDictValueList.get(0).setIsDefault(true);
        return sysDictValueList;
    }

    /**
     * 查看数据字典的所有值
     * @return
     */
    @Override
    public List<Map<String, String>> findAllDictValue () {
        return dictValueRepository.findAllDictValue();
    }

}
