package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.sys.model.SysDictValue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictValueRepository extends BaseRepository<SysDictValue, Integer> {

    List<SysDictValue> findByParentId(Integer parentId);

    List<SysDictValue> findByEnabled(Boolean enabled);

}
