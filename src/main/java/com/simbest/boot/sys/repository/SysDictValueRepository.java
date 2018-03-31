package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.sys.model.SysDictValue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictValueRepository extends BaseRepository<SysDictValue, Long> {

    List<SysDictValue> findByParentId(Long parentId);

    List<SysDictValue> findByEnabled(Boolean enabled);

    List<SysDictValue> findByDictId(Long dictId);

    List<SysDictValue> findByDictIdAndEnabledAndRemoved(Long dictId, Boolean enabled, Boolean removed);
}
