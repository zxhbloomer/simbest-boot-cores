/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service;

import com.simbest.boot.security.auth.model.SysDuty;

import java.util.List;

/**
 * <strong>Description : 基于Spring Security的职务逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
public interface SysDutyService {

    List<SysDuty> findAll();

    SysDuty findById(Integer id);

    SysDuty save(SysDuty duty);
}
