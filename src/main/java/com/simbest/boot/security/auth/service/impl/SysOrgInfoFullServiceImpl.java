/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.simbest.boot.base.service.impl.GenericService;
import com.simbest.boot.security.auth.model.SysOrgInfoFull;
import com.simbest.boot.security.auth.repository.SysOrgInfoFullRepository;
import com.simbest.boot.security.auth.service.SysOrgInfoFullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <strong>Description : 基于Spring Security的组织逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Service
public class SysOrgInfoFullServiceImpl extends GenericService<SysOrgInfoFull,Integer> implements SysOrgInfoFullService {

    @Autowired
    private SysOrgInfoFullRepository orgRepository;


    @Override
    public void deleteById(Integer id) {
        orgRepository.deleteById(id);
    }
}
