package com.simbest.boot.base.service.impl;

import com.google.common.collect.Lists;
import com.simbest.boot.base.model.SystemModel;
import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.base.service.ISystemService;
import com.simbest.boot.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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
public class SystemService<T extends SystemModel,PK extends Serializable> extends GenericRepositoryService<T,PK> implements ISystemService<T,PK> {

    private BaseRepository<T,PK> baseRepository;

    @Autowired
    private ApplicationContext appContext;

    public SystemService () {
        baseRepository = (BaseRepository<T,PK> )appContext.getBean("baseRepository");
    }

    @Override
    public T save ( T o) {
        String operatorFlag = o.getOperatorFlag();
        if ( "add".equals( operatorFlag ) ){
            wrapCreateInfo( o );
        } else {
            wrapUpdateInfo( o );
        }
        return super.save( o );
    }

    @Override
    public <S extends T> List<S> saveAll ( Iterable<? extends T> iterable ) {
        List<S> dataList = Lists.newArrayList();
        for(T t:iterable){
            dataList.add((S)this.save( t ));
        }
        return dataList;
    }

    protected void wrapUpdateInfo( T o) {
        o.setModifiedTime(DateUtil.getCurrent());
    }

    protected void wrapCreateInfo(T o) {
        o.setCreatedTime(DateUtil.getCurrent());
        o.setModifiedTime(DateUtil.getCurrent());
        wrapUpdateInfo(o);
    }
}
