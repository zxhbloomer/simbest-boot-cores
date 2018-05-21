package com.simbest.boot.base.repository;

import com.simbest.boot.base.model.GenericModel;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <strong>Title : 业务实体通用数据持久层</strong><br>
 * <strong>Description : 业务实体通用数据持久层</strong><br>
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
@Repository
public interface LogicRepository<T extends GenericModel, PK extends Serializable> extends BaseRepository<T, PK>{


}
