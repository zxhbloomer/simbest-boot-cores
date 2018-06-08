/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/6/8  18:08
 */
@Transactional
@NoRepositoryBean
public interface LogicDeleteRepository<T, PK extends Serializable> extends BaseRepository<T, PK> {

    long countActive();

    long countActive(Specification<T> conditions);

    boolean existsActive(PK id);

    Page<T> findAllActive();

    Page<T> findAllActive(Sort sort);

    Page<T> findAllActive(Pageable pageable);

    List<T> findAllActive(Iterable<PK> ids);

    Page<T> findAllActive(Specification<T> conditions, Pageable pageable);

    T findOneActive(PK id);

    @Modifying
    void logicDelete(PK id);

    @Modifying
    void logicDelete(T entity);

    @Modifying
    void logicDelete(Iterable<? extends T> entities);

    @Modifying
    void logicDeleteAll();

    void deleteAllByIds ( Iterable<? extends PK> ids );

    @Modifying
    void scheduleLogicDelete(PK id, LocalDateTime localDateTime);

    @Modifying
    void scheduleLogicDelete(T entity, LocalDateTime localDateTime);

}
