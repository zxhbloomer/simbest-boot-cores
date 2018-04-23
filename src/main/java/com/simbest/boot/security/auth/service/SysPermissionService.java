/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service;

import com.simbest.boot.security.auth.model.SysPermission;

import java.util.List;
import java.util.Map;

/**
 * <strong>Description : 基于Spring Security的权限逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
public interface SysPermissionService {

    /**
     * 返回用户可见菜单
     *
     * @param username 用户登录标识
     * @return 用户菜单List<Map>
     */
    List<Map<String, Object>> findMenu(String username);

    List<SysPermission> findAll();
}
