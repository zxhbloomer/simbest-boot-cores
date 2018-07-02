package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.sys.model.SysDictValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysDictValueRepository extends LogicRepository<SysDictValue, Integer> {

    List<SysDictValue> findByParentId(Integer parentId);

    List<SysDictValue> findByEnabled(Boolean enabled);


    /**
     * 根据字典类型查询数据字典中的相应值name以及value的值
     */
    String sql1 = " SELECT dv.* from sys_dict d,sys_dict_value dv where d.id=dv.dict_id and d.enabled=1 and dv.enabled=1 and d.id=:dictId"+
            " order by dv.display_order asc ";
    @Query (value = sql1,nativeQuery = true)
    List<SysDictValue> findDictValue(@Param ("dictId") Integer dictId);

    /**
     *根据字典类型以及值的父类型来查询数据字典中的相应值name以及value的值
     */
    String sql2 = " SELECT dv.* from sys_dict d,sys_dict_value dv where d.id=dv.dict_id and d.enabled=1 and dv.enabled=1 " +
            " and d.id=:dictId and dv.parent_id=:parentId"+
            " order by dv.display_order asc ";
    @Query (value = sql2,nativeQuery = true)
    List<SysDictValue> findDictValue(@Param ("dictId") Integer dictId,@Param ("parentId")Integer parentId);

    /**
     *根据字典类型以及值的父类型来查询数据字典中的相应值name以及value的值
     */
    String sql4 = " SELECT dv.name as name,dv.value as name,dv.isDefault as isDefault from sys_dict d,sys_dict_value dv where d.id=dv.dict_id and d.enabled=1 and dv.enabled=1 " +
            " and d.id=:dictId and dv.parent_id=:parentId"+
            " order by dv.display_order asc ";
    @Query (value = sql4,nativeQuery = true)
    List<Map<String,String>> findDictValue2(@Param ("dictId") Integer dictId,@Param ("parentId")Integer parentId);

    /**
     * 查看数据字典的所有值
     */
    String sql3 = " SELECT dv.*,d.name as dict_type_name from sys_dict d,sys_dict_value dv where d.id=dv.dict_id and d.enabled=1 and dv.enabled=1 "+
            " order by d.display_order asc, dv.display_order asc ";
    @Query (value = sql3,nativeQuery = true)
    List<Map<String,String>> findAllDictValue();

}
