package com.simbest.boot.sys.service.impl;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.sys.model.SysDictValue;
import com.simbest.boot.sys.repository.SysDictValueRepository;
import com.simbest.boot.sys.service.ISysDictValueService;
import com.simbest.boot.util.security.SecurityUtils;
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
        save(val);
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

    @Override
    public SysDictValue save(SysDictValue dictValue) {
        String username = SecurityUtils.getCurrentUserName();
        if (dictValue.getId() == null) {
            dictValue.setCreator(username);
        }
        dictValue.setModifier(username);
        return dictValueRepository.save(dictValue);
    }

    @Override
    public void deleteById(String id) {
        SysDictValue dictValue = findById(id);
        save(dictValue);
        List<SysDictValue> list = findByParentId(id);
        for (SysDictValue v : list) {
            deleteById(v.getId());
        }
    }

    /**
     *
     */
    @Override
    public Page findAll(Specification<SysDictValue> conditions, Pageable pageable) {
        return dictValueRepository.findAll(conditions, pageable);
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

    /**
     *
     */
    @Override
    public Page findAll(Pageable pageable) {
        return dictValueRepository.findAll(pageable);
    }

    /**
     *  //@see com.simbest.boot.ddy.service.IPlanService:getPageable(page, size, direction, properties);
     */
    @Override
    public Pageable getPageable(int page, int size, String direction, String properties) {
        return dictValueRepository.getPageable(page, size, direction, properties);
    }

    /**
     * // @see com.simbest.boot.ddy.service.IPlanService:getSpecification(Condition conditions);
     */
    @Override
    public Specification<SysDictValue> getSpecification(Condition conditions) {
        return dictValueRepository.getSpecification(conditions);
    }

    /**
     * // @see com.simbest.boot.ddy.service.IPlanService:getSpecification(SysDictValue conditions);
     */
    @Override
    public Specification<SysDictValue> getSpecification(SysDictValue conditions) {
        return dictValueRepository.getSpecification(conditions);
    }

}
