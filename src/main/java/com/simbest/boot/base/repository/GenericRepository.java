package com.simbest.boot.base.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <strong>Title : 基础实体通用数据库持久层</strong><br>
 * <strong>Description : 基础实体通用数据库持久层</strong><br>
 * <strong>Create on : 2018/5/18</strong><br>
 * <strong>Modify on : 2018/5/18</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Transactional
@NoRepositoryBean
public interface GenericRepository<T, PK extends Serializable> extends BaseRepository<T, PK>{


}
