/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.sys.model.SysCustomField;
import org.springframework.stereotype.Repository;

/**
 * 用途：实体自定义字段持久层
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Repository
public interface SysCustomFieldRepository extends BaseRepository<SysCustomField, Long> {

    SysCustomField findByFieldClassify(String fieldClassify);
}
