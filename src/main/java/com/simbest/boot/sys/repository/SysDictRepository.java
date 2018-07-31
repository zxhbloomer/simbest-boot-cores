package com.simbest.boot.sys.repository;

import com.simbest.boot.sys.model.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

    //    String SQL = "SELECT * FROM sys_dict WHERE parent_id=:id";
//    @Query(value = SQL, nativeQuery = true)
//    List<SysDict> findByParentId(@Param("id")Long parentId);
    List<SysDict> findByParentId(Integer parentId);

    List<SysDict> findByParentIdAndEnabled(Integer parentId, Boolean enabled);

    List<SysDict> findByEnabled(Boolean enabled);


}
