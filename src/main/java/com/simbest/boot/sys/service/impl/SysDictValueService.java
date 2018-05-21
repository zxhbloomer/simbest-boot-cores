package com.simbest.boot.sys.service.impl;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.service.impl.GenericRepositoryService;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import com.simbest.boot.sys.model.SysDictValue;
import com.simbest.boot.sys.repository.SysDictValueRepository;
import com.simbest.boot.sys.service.ISysDictValueService;
import com.simbest.boot.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDictValueService implements ISysDictValueService {

    @Autowired
    private SysDictValueRepository dictValueRepository;

    @Autowired
    private SysUserInfoFullService userService;


    @Override
    public int updateEnableByDictId(boolean enabled, Long dictId) {
        List<SysDictValue> list = dictValueRepository.findByDictId(dictId);
        for (SysDictValue val : list) {
            val.setEnabled(enabled);
            dictValueRepository.save(val);
        }
        return 1;
    }

    @Override
    public int updateEnable(boolean enabled, Long dictValueId) {
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
    public List<SysDictValue> findByDictId(Long dictId) {
        return dictValueRepository.findByDictIdAndEnabledAndRemoved(dictId, true, false);
    }

    public Map<Long, SysDictValue> findByDictIdForKV(Long dictId) {
        List<SysDictValue> values = dictValueRepository.findByDictIdAndEnabledAndRemoved(dictId, true, false);
        Map<Long, SysDictValue> map = new HashMap<>();
        for (SysDictValue v : values) {
            map.put(v.getId(), v);
        }
        return map;
    }

    @Override
    public List<SysDictValue> findByParentId(Long parentId) {
        return dictValueRepository.findByParentId(parentId);
    }

    public SysDictValue findById(Long id) {
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
    public void deleteById(Long id) {
        SysDictValue dictValue = findById(id);
        dictValue.setRemoved(true);
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
     *
     */
    @Override
    public Page findAll(Pageable pageable) {
        return dictValueRepository.findAll(pageable);
    }

    /**
     * @see com.simbest.boot.ddy.service.IPlanService:getPageable(page, size, direction, properties);
     */
    @Override
    public Pageable getPageable(int page, int size, String direction, String properties) {
        return dictValueRepository.getPageable(page, size, direction, properties);
    }

    /**
     * @see com.simbest.boot.ddy.service.IPlanService:getSpecification(Condition conditions);
     */
    @Override
    public Specification<SysDictValue> getSpecification(Condition conditions) {
        return dictValueRepository.getSpecification(conditions);
    }

    /**
     * @see com.simbest.boot.ddy.service.IPlanService:getSpecification(SysDictValue conditions);
     */
    @Override
    public Specification<SysDictValue> getSpecification(SysDictValue conditions) {
        return dictValueRepository.getSpecification(conditions);
    }

}
