package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.sys.model.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictRepository extends BaseRepository<SysDict, Long> {

    //    String SQL = "SELECT * FROM sys_dict WHERE parent_id=:id";
//    @Query(value = SQL, nativeQuery = true)
//    List<SysDict> findByParentId(@Param("id")Long parentId);
    List<SysDict> findByParentId(Long parentId);

    List<SysDict> findByParentIdAndEnabledAndRemoved(Long parentId, Boolean enabled, Boolean removed);

    List<SysDict> findByEnabled(Boolean enabled);


}
