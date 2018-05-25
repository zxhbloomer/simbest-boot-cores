/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.service.impl;

import com.simbest.boot.base.service.impl.GenericService;
import com.simbest.boot.security.auth.model.SysUserInfo;
import com.simbest.boot.security.auth.model.SysUserInfoFull;
import com.simbest.boot.security.auth.repository.SysOrgInfoFullRepository;
import com.simbest.boot.security.auth.repository.SysRoleRepository;
import com.simbest.boot.security.auth.repository.SysPermissionRepository;
import com.simbest.boot.security.auth.repository.SysUserInfoFullRepository;
import com.simbest.boot.security.auth.service.SysUserInfoFullService;
import com.simbest.boot.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * <strong>Description : 基于Spring Security的人员信息逻辑层</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Service
public class SysUserInfoFullServiceImpl extends GenericService<SysUserInfoFull,Integer> implements SysUserInfoFullService {

    @Autowired
    private SysUserInfoFullRepository userRepository;

    @Autowired
    private SysOrgInfoFullRepository orgRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysPermissionRepository permissionRepository;

    @Override
    public SysUserInfo getCurrentUser() {
        return loadUserByUsername(SecurityUtils.getCurrentUserName());
    }

    @Override
    public SysUserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserInfoFull userInfo = userRepository.findByUsername(username);
        if(userInfo == null)
            throw new UsernameNotFoundException(username + " not found!");
        userInfo.setOrgs(orgRepository.getOrgByUsername(username));
        userInfo.setAuthRoles(roleRepository.getRoleByUsername(username));
        userInfo.setAuthPermissions(permissionRepository.getPermissionByUsername(username));
        return userInfo;
    }

    /**
     * 获取所有用户信息
     *
     * @return 用户信息
     */
    @Override
    public List<SysUserInfoFull> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据主键删除用户
     *
     * @param id 用户Id
     */
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     *
     */
    @Override
    public Page<SysUserInfoFull> findAll(Specification<SysUserInfoFull> conditions, PageRequest pageable) {
        return userRepository.findAll(conditions, pageable);
    }

    /**
     *
     */
    @Override
    public SysUserInfoFull save(SysUserInfoFull user) {
        return userRepository.save(user);
    }

    /**
     *
     */
    @Override
    public SysUserInfoFull findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 认证权限比较器
     */
    private static class NameGrantedAuthorityComparator implements
            Comparator<GrantedAuthority> {
        @Override
        public int compare(GrantedAuthority o1, GrantedAuthority o2) {
            return o1.equals(o2) ? 0 : -1;
        }
    }

}
