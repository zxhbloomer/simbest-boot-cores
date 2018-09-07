package com.simbest.boot.base.service.impl;

import com.google.common.collect.Lists;
import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.exceptions.InsertExistObjectException;
import com.simbest.boot.exceptions.UpdateNotExistObjectException;
import com.simbest.boot.util.CustomBeanUtil;
import com.simbest.boot.util.ObjectUtil;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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

    public LogicService ( LogicRepository<T, PK> logicRepository) {
        super(logicRepository);
        this.logicRepository = logicRepository;
    }

    @Override
    public long count ( ) {
        log.debug("@Logic Repository Service count null param");
        return logicRepository.countActive();
    }

    @Override
    public long count ( Specification<T> specification ) {
        log.debug("@Logic Repository Service count Specification param");
        return logicRepository.countActive( specification );
    }

    @Override
    public boolean exists ( PK id ) {
        log.debug("@Logic Repository Service exists object by id: " + id);
        return logicRepository.existsActive( id );
    }

    @Override
    public T findOne ( PK id ){
        log.debug("@Logic Repository Service getOne");
        return logicRepository.findOneActive(id);
    }

    @Override
    public T findById ( PK id ){
        log.debug("@Logic Repository Service findById");
        return logicRepository.findOneActive(id);
    }

    @Override
    public Page<T> findAll ( ) {
        log.debug("@Logic Repository Service findAll");
        return logicRepository.findAllActive();
    }

    @Override
    public Page<T>  findAll ( Pageable pageable ) {
        log.debug("@Logic Repository Service findAll object PageSize:" + pageable.getPageSize() + ":PageNumber:" + pageable.getPageNumber());
        return logicRepository.findAllActive( pageable );
    }

    @Override
    public Page<T>  findAll ( Sort sort ) {
        log.debug("@Logic Repository Service object by Sort");
        return logicRepository.findAllActive(PageRequest.of(ApplicationConstants.DEFAULT_PAGE, ApplicationConstants.DEFAULT_SIZE, sort));
    }

    @Override
    public List<T> findAllByIDs(Iterable<PK> ids) {
        log.debug("@Logic Repository Service object by findAllByIDs");
        return logicRepository.findAllActive(ids);
    }

    @Override
    public Page<T> findAll (Specification<T> conditions, Pageable pageable ) {
        log.debug("@Logic Repository Service findAll");
        return logicRepository.findAllActive(conditions, pageable);
    }

    @Override
    @Transactional
    public T updateEnable (PK id, boolean enabled) {
        T obj =  super.findById( id );
        if (obj == null) {
            return null;
        }
        obj.setEnabled( enabled );
        return update(obj);
    }

    @Override
    @Transactional
    public T insert ( T source) {
        if(null == ObjectUtil.getEntityIdVaue(source)) {
            log.debug("@Logic Repository Service create new object: " + source);
            wrapCreateInfo(source);
            T target = logicRepository.save(source);
            CustomBeanUtil.copyTransientProperties(source,target);
            return target;
        } else {
            throw new InsertExistObjectException();
        }
    }

    @Override
    @Transactional
    public T update ( T source) {
        PK pk = (PK)ObjectUtil.getEntityIdVaue(source);
        if(null != pk) {
            log.debug("@Logic Repository Service update a already object: " + source);
            T target = findById(pk);
            CustomBeanUtil.copyPropertiesIgnoreNull(source, target);
            wrapUpdateInfo( target );
            T newTarget = logicRepository.save(target);
            CustomBeanUtil.copyTransientProperties(target,newTarget);
            return newTarget;
        } else {
            throw new UpdateNotExistObjectException();
        }
    }

    @Override
    @Transactional
    public List<T> saveAll(Iterable<T> entities) {
        log.debug("@Logic Repository Service saveAll");
        List<T> list = Lists.newArrayList();
        for(T o : entities){
            o = insert(o);
            list.add(o);
        }
        return list;
    }

    @Override
    @Transactional
    public void deleteById ( PK id ) {
        T o = findById(id);
        wrapUpdateInfo(o);
        log.debug("@Logic Repository Service deleteById object by id: " + id);
        logicRepository.logicDelete( id );
    }

    @Override
    @Transactional
    public void delete ( T o ) {
        log.debug("@Logic Repository Service delete object: " + o);
        wrapUpdateInfo( o );
        logicRepository.logicDelete( o );
    }

    @Override
    @Transactional
    public void deleteAll ( Iterable<? extends T> iterable ) {
        log.debug("@Logic Repository Service deleteAll Iterable param");
        iterable.forEach( o -> delete(o));
    }

    @Override
    @Transactional
    public void deleteAll ( ) {
        log.debug("@Logic Repository Service deleteAll null param");
        Iterable<? extends T> iterable = findAllNoPage();
        deleteAll(iterable);
    }

    @Override
    @Transactional
    public void deleteAllByIds ( Iterable<? extends PK> pks ) {
        log.debug("@Logic Repository Service deleteAllByIds Iterable param");
        pks.forEach( pk -> deleteById(pk));
    }

    @Override
    @Transactional
    public void scheduleLogicDelete(PK id, LocalDateTime localDateTime) {
        log.debug("@Logic Repository Service schedule logic delete object with id: %s at %s", id, localDateTime.now());
        logicRepository.scheduleLogicDelete(id, localDateTime);
    }

    @Override
    @Transactional
    public void scheduleLogicDelete(T entity, LocalDateTime localDateTime) {
        log.debug("@Logic Repository Service schedule logic delete object : %s at %s", entity, localDateTime.now());
        logicRepository.scheduleLogicDelete(entity, localDateTime);
    }

    protected void wrapCreateInfo(T o) {
        String userName = SecurityUtils.getCurrentUserName();
        o.setCreator(userName);
        wrapUpdateInfo(o);
    }

    protected void wrapUpdateInfo(T o) {
        String userName = SecurityUtils.getCurrentUserName();
        o.setModifier(userName);
    }


}
