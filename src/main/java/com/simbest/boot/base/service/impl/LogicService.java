package com.simbest.boot.base.service.impl;

import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * <strong>Title : 业务实体通用服务层</strong><br>
 * <strong>Description : 涉及业务实体的所有操作需要记录创建人信息和更新人信息</strong><br>
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
public class LogicService<T extends LogicModel,PK extends Serializable> extends SystemService<T,PK> implements ILogicService<T,PK> {

    private LogicRepository<T,PK> logicRepository;

    public LogicService ( LogicRepository<T, PK> logicRepository ) {
        super(logicRepository);
        this.logicRepository = logicRepository;
    }

    /**
     * @see
     * @param enabled
     * @param id
     * @return
     */
    @Override
    public int updateEnable ( boolean enabled, PK id ) {
        T obj =  super.getById( id );
        if (obj == null) {
            return 0;
        }
        obj.setEnabled( enabled );
        wrapUpdateStateInfo(obj);
        logicRepository.save( obj );
        return 1;
    }

    public T save ( T o) {
        wrapCreateInfo( o );
        return logicRepository.save( o );
    }

    /**
     * @see
     * @param o
     * @return
     */
    public int deleteLogic(T o) {
        int flag = 0;
        if(!StringUtils.isEmpty(o)){
            log.debug("@Logic Service delete objects by object: "+ o);
            wrapUpdateInfo(o);
            logicRepository.save( o );
            flag = 1;
        }
        return flag;
    }

    @Override
    public void deleteAll ( Iterable<? extends T> iterable ) {
        for(T t:iterable){
            wrapUpdateInfo(t);
            super.save( t );
        }
    }

    protected void wrapUpdateStateInfo(T o){
        String userName = SecurityUtils.getCurrentUserName();
        o.setModifier(userName);
        o.setModifiedTime(DateUtil.getCurrent());
    }

    protected void wrapUpdateInfo(T o) {
        String userName = SecurityUtils.getCurrentUserName();
        o.setModifier(userName);
        o.setModifiedTime(DateUtil.getCurrent());
    }

    protected void wrapCreateInfo(T o) {
        String userName = SecurityUtils.getCurrentUserName();
        o.setCreator(userName);
        o.setCreatedTime(DateUtil.getCurrent());
        o.setModifiedTime(DateUtil.getCurrent());
        wrapUpdateInfo(o);
    }
}
