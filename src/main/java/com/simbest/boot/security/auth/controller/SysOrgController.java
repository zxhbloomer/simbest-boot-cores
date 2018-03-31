package com.simbest.boot.security.auth.controller;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.security.auth.model.SysOrgInfoFull;
import com.simbest.boot.security.auth.repository.SysOrgInfoFullRepository;
import com.simbest.boot.security.auth.repository.SysUserInfoFullRepository;
import com.simbest.boot.security.auth.service.SysOrgInfoFullService;
import com.simbest.boot.util.CustomBeanUtil;
import io.swagger.annotations.Api;
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

import java.util.List;
import java.util.UUID;

/**
 * <strong>Title</strong> : SysOrgController.java<br>
 * <strong>Description</strong> : <br>
 * <strong>Create on</strong> : 2018/02/27<br>
 * <strong>Modify on</strong> : 2018/03/02<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@Api(description = "系统组织操作相关接口")
@Slf4j
@RestController
@RequestMapping("/sys/org")
public class SysOrgController {

    @Autowired
    private SysOrgInfoFullRepository sysOrgRepository;

    @Autowired
    private SysOrgInfoFullService sysOrgService;

    @Autowired
    private SysUserInfoFullRepository sysUserRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "新建组织信息", notes = "通过此接口来新建组织信息")
    @PostMapping(value = "/create")
    public JsonResponse create(@RequestBody final SysOrgInfoFull sysOrg) {
        if (sysOrg == null) {
            return JsonResponse.defaultErrorResponse();
        }
        sysOrg.setOrgCode(UUID.randomUUID().toString());
        sysOrgRepository.save(sysOrg);
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "删除组织信息", notes = "通过此接口来删除组织信息")
    @ApiImplicitParam(name = "id", value = "组织ID", required = true, dataType = "Integer", paramType = "path")
    @PostMapping(value = "/delete/{id}")
    public JsonResponse delete(@PathVariable final int id) {
        sysOrgService.deleteById(id);
        return JsonResponse.defaultSuccessResponse();
    }


    /**
     * @param ids   ids
     * @return JsonResponse
     */
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "删除组织信息", notes = "通过此接口来批量删除组织信息")
    @ApiImplicitParam(name = "ids", value = "组织ID", required = true, dataType = "String", paramType = "query")
    @PostMapping(value = "/deletes")
    public JsonResponse deletes(@RequestParam String ids) {
        String[] s = ids.split(",");
        for (String t : s) {
            int id = Integer.valueOf(t);
            sysOrgService.deleteById(id);
        }
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "修改组织信息", notes = "通过此接口来修改组织信息")
    @ApiImplicitParam(name = "id", value = "组织ID", required = true, dataType = "Integer", paramType = "path")
    @PostMapping(value = "/update/{id}")
    public JsonResponse update(@PathVariable final int id, @RequestBody final SysOrgInfoFull sysOrg) {
        SysOrgInfoFull sysOrgDb = sysOrgRepository.findById(id).orElse(null);
        CustomBeanUtil.copyPropertiesIgnoreNull(sysOrg, sysOrgDb);
        sysOrgRepository.save(sysOrgDb);
        return JsonResponse.defaultSuccessResponse();
    }

    @ApiOperation(value = "查询组织信息", notes = "通过此接口来查询组织信息")
    @ApiImplicitParam(name = "id", value = "组织ID", required = true, dataType = "Long", paramType = "path")
    @PostMapping(value = "/query/{id}")
    public JsonResponse query(@PathVariable final int id) {
        SysOrgInfoFull org = sysOrgRepository.findById(id).orElse(new SysOrgInfoFull());
        return JsonResponse.success(org);
    }

    @ApiOperation(value = "获取组织列表", notes = "通过此接口来获取组织列表")
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
                                  @RequestParam(required = false, defaultValue = "") String fullName //
    ) {

        // 获取分页规则
        Pageable pageable = sysOrgRepository.getPageable(page, size, direction, properties);

        // 获取查询条件
        Condition condition = new Condition();
        condition.like("fullName", "%" + fullName + "%");
        Specification<SysOrgInfoFull> s = sysOrgRepository.getSpecification(condition);

        // 获取查询结果
        Page<SysOrgInfoFull> pages = sysOrgRepository.findAll(s, pageable);

        return JsonResponse.success(pages);
    }

    @ApiOperation(value = "获取组织列表", notes = "通过此接口来获取组织列表")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "id", value = "默认组织Id", dataType = "int", paramType = "query", //
                    required = false, example = "1") //
    })
    @PostMapping(value = "/queryTree")
    public JsonResponse queryTree(@RequestParam(required = false, defaultValue = "1") int id) {
        List<SysOrgInfoFull> pages = sysOrgRepository.findAll();
        return JsonResponse.success(pages);
    }


}
