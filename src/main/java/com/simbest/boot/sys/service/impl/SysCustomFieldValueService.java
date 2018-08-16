/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.service.impl;

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.sys.model.SysCustomFieldValue;
import com.simbest.boot.sys.repository.SysCustomFieldValueRepository;
import com.simbest.boot.sys.service.ISysCustomFieldValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用途：实体自定义字段值逻辑层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Service
public class SysCustomFieldValueService extends LogicService<SysCustomFieldValue, String> implements ISysCustomFieldValueService {

    private SysCustomFieldValueRepository fieldValueRepository;

    @Autowired
    public SysCustomFieldValueService(SysCustomFieldValueRepository fieldValueRepository) {
        super(fieldValueRepository);
        this.fieldValueRepository = fieldValueRepository;
    }

}
