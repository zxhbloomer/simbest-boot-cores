/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service;


import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.security.auth.model.SysOrgInfoFull;

import javax.transaction.Transactional;
import java.util.List;

/**
 * <strong>Description : 基于Spring Security的组织逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Transactional
public interface SysOrgInfoFullService extends ILogicService<SysOrgInfoFull,Integer> {

    void deleteById(Integer id);

    /**
     * 获取根组织
     * @return
     */
    SysOrgInfoFull getRoot();

    /**
     * 获取下属部门（不包括传入部门）
     * @param id
     * @return
     */
    List<SysOrgInfoFull> getChildrenOrg( Integer id);

    /**
     * 通过父亲部门获取部门
     * @param parentId
     * @return
     */
    List<SysOrgInfoFull> getByParent(Integer parentId);

    /**
     * 根据组件编号查询组织基本信息
     * @param orgCode   组织编码
     * @return
     */
    SysOrgInfoFull getByOrgCode(String orgCode);
}
