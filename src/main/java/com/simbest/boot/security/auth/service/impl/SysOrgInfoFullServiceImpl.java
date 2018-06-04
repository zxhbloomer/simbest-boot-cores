/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.google.common.collect.Lists;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.security.auth.model.SysOrgInfoFull;
import com.simbest.boot.security.auth.repository.SysOrgInfoFullRepository;
import com.simbest.boot.security.auth.service.SysOrgInfoFullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <strong>Description : 基于Spring Security的组织逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Service
public class SysOrgInfoFullServiceImpl extends LogicService<SysOrgInfoFull,Integer> implements SysOrgInfoFullService {


    private SysOrgInfoFullRepository orgRepository;

    @Autowired
    public SysOrgInfoFullServiceImpl ( SysOrgInfoFullRepository sysOrgInfoFullRepository ) {
        super( sysOrgInfoFullRepository );
        this.orgRepository = sysOrgInfoFullRepository;
    }


    @Override
    public void deleteById(Integer id) {
        orgRepository.deleteById(id);
    }

    /**
     * 获取根组织
     * @return
     */
    @Override
    public SysOrgInfoFull getRoot ( ) {
        return orgRepository.getRoot();
    }

    @Override
    public List<SysOrgInfoFull> getChildrenOrg ( Integer id ) {
        List<SysOrgInfoFull> result = Lists.newArrayList();
        Optional<SysOrgInfoFull> parent = orgRepository.findById(id);
        if(parent.isPresent()){
            List<SysOrgInfoFull> children = getByParent(parent.get().getId());
            result.addAll(children);
            for(SysOrgInfoFull child:children){
                result.addAll(getChildrenOrg(child.getId()));
            }
        }
        return result;
    }

    @Override
    public List<SysOrgInfoFull> getByParent ( Integer parentId ) {
        return orgRepository.getByParent( parentId );
    }

    /**
     * 根据组件编号查询组织基本信息
     * @param orgCode   组织编码
     * @return
     */
    @Override
    public SysOrgInfoFull getByOrgCode ( String orgCode ) {
        return null;
    }
}
