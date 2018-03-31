/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.controller;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.security.auth.model.SysPermission;
import com.simbest.boot.security.auth.repository.SysPermissionRepository;
import com.simbest.boot.security.auth.service.SysPermissionService;
import com.simbest.boot.util.CustomBeanUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：权限信息控制器
 * 作者: lishuyi
 * 时间: 2018/1/31  15:49
 */
@Slf4j
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Autowired
    private SysPermissionService permissionService;

    @PreAuthorize("hasAuthority('ROLE_SUPER')")
    @ApiOperation(value = "新建权限信息", notes = "通过此接口来新建权限信息")
    @PostMapping(value = "/create")
    public JsonResponse create(@RequestBody final SysPermission sysPermission) {
        sysPermissionRepository.save(sysPermission);
        return JsonResponse.success(sysPermission);
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER')")
    @ApiOperation(value = "删除权限信息", notes = "通过此接口来删除权限信息")
    @ApiImplicitParam(name = "id", value = "权限ID", required = true, dataType = "Integer", paramType = "path")
    @PostMapping(value = "/delete/{id}")
    public JsonResponse delete(@PathVariable final int id) {
        sysPermissionRepository.deleteById(id);
        return JsonResponse.defaultSuccessResponse();
    }


    /**
     * @param ids   ids
     * @return JsonResponse
     */
    @PreAuthorize("hasAuthority('ROLE_SUPER')")  // 指定角色权限才能操作方法
    @ApiOperation(value = "删除权限信息", notes = "通过此接口来批量删除权限信息")
    @ApiImplicitParam(name = "ids", value = "组织ID", required = true, dataType = "String", paramType = "query")
    @PostMapping(value = "/deletes")
    public JsonResponse deletes(@RequestParam String ids) {
        String[] s = ids.split(",");
        for (String t : s) {
            int id = Integer.valueOf(t);
            sysPermissionRepository.deleteById(id);
        }
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER')")  // 指定角色权限才能操作方法
    @ApiOperation(value = "修改权限信息", notes = "通过此接口来修改权限信息")
    @ApiImplicitParam(name = "id", value = "组织ID", required = true, dataType = "Integer", paramType = "path")
    @PostMapping(value = "/update/{id}")
    public JsonResponse update(@PathVariable final int id, @RequestBody final SysPermission sysPermission) {
        SysPermission sysPermissionDb = sysPermissionRepository.findById(id).orElse(null);
        CustomBeanUtil.copyPropertiesIgnoreNull(sysPermission, sysPermissionDb);
        sysPermissionRepository.save(sysPermissionDb);
        return JsonResponse.defaultSuccessResponse();
    }

    @ApiOperation(value = "查询权限信息", notes = "通过此接口来查询权限信息")
    @ApiImplicitParam(name = "id", value = "权限ID", required = true, dataType = "Long", paramType = "path")
    @PostMapping(value = "/query/{id}")
    public JsonResponse query(@PathVariable final int id) {
        SysPermission permission = sysPermissionRepository.findById(id).orElse(new SysPermission());
        return JsonResponse.success(permission);
    }

    @ApiOperation(value = "获取权限列表", notes = "通过此接口来获取权限列表")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", //
                    required = true, example = "1"), //
            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
                    required = true, example = "10"), //
            @ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", //
                    paramType = "query") //
    })
    @PostMapping(value = "/queryList")
    public JsonResponse queryList(@RequestParam(required = false, defaultValue = "1") int page, //
                                  @RequestParam(required = false, defaultValue = "10") int size, //
                                  @RequestParam(required = false) String direction, //
                                  @RequestParam(required = false) String properties, //
                                  @RequestParam(required = false, defaultValue = "") String description //
    ) {

        // 获取分页规则
        Pageable pageable = sysPermissionRepository.getPageable(page, size, direction, properties);

        // 获取查询条件
        Condition condition = new Condition();
        condition.like("description", "%" + description + "%");
        Specification<SysPermission> s = sysPermissionRepository.getSpecification(condition);

        // 获取查询结果
        Page<SysPermission> pages = sysPermissionRepository.findAll(s, pageable);

        return JsonResponse.success(pages);
    }
}
