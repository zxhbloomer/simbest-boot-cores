package com.simbest.boot.base.service.impl;

import com.google.common.collect.Lists;
import com.simbest.boot.base.model.SystemModel;
import com.simbest.boot.base.repository.SystemRepository;
import com.simbest.boot.base.service.ISystemService;
import com.simbest.boot.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * <strong>Title : 系统实体通用服务层</strong><br>
 * <strong>
 *     Description : 需要记录数据操作的创建时间和更新时间
 * </strong><br>
 * <strong>Create on : 2018/5/17</strong><br>
 * <strong>Modify on : 2018/5/17</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Slf4j
public class SystemService<T extends SystemModel,PK extends Serializable> extends GenericService<T,PK> implements ISystemService<T,PK> {

    private SystemRepository<T,PK> systemRepository;

    public SystemService(){}

    public SystemService (SystemRepository<T, PK> systemRepository ) {
        super(systemRepository);
        this.systemRepository = systemRepository;
    }

//    @Override
//    public List<T> saveAll(Iterable<T> entities) {
//        log.debug("@SystemService saveAll");
//        List<S> list = Lists.newArrayList();
//        for(S o : entities){
//            o = (S) insert(o);
//            list.add(o);
//        }
//        return list;
//    }

}
