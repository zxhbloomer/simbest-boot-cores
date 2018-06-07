/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.google.common.collect.Maps;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.sys.model.SysDict;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.util.redis.RedisCacheUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用途：数据字典控制器
 * 作者: zlxtk
 * 时间: 2018/2/22  10:14
 */
@Slf4j
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    private ISysDictService dictService;


    /**
     * 获取字典树
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryDict")
    @ResponseBody
    public List<SysDict> queryDict() {
        List<SysDict> roots = dictService.findByAll();
        return roots;
    }


    /**
     * 添加同级
     *
     * @param dict 要新增的字典
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse create(@RequestBody SysDict dict) {
        if (dict.getId() != null) {
            SysDict selectDict = dictService.findById(dict.getId());
            if (selectDict == null) {
                return JsonResponse.defaultErrorResponse();
            }
            dict.setParentId(selectDict.getParentId());
            dict.setId(null);
        }
        SysDict newDict = dictService.save(dict);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 新增下级 "sys/dict/createChild"
     *
     * @param dict
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/createChild", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse createChild(@RequestBody SysDict dict) {
        if (dict == null) {
            return JsonResponse.defaultErrorResponse();
        }
        dict.setParentId(dict.getId());
        dict.setId(null);
        SysDict newDict = dictService.save(dict);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 修改
     *
     * @param dict
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse update(@RequestBody SysDict dict) {
        dictService.save(dict);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 获取json格式数据字典
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")  // 指定角色权限才能操作方法
    @PostMapping(value = "/json/list")
    @ResponseBody
    public JsonResponse listJson() {
        RedisCacheUtils.saveString("hello", "lishuyi指定角色权限才能操作方法");
        RedisCacheUtils.saveString("hello100", "lishuyi指定角色权限才能操作方法", 100);
        List<SysDict> list = dictService.findByEnabled(true);
        return JsonResponse.builder().errcode(JsonResponse.SUCCESS_CODE).message("OK").data(list).build();
    }

    @ApiOperation(value = "查询字段树", notes = "通过此接口来查询字段树信息")
    @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/select/tree", method = RequestMethod.GET)
    public ModelAndView getSelectTree() {
        // 获取查询结果
        List<SysDict> pages = dictService.findByEnabled(true);

        Map<String, Object> map = new HashMap<>();
        map.put("list", pages);
        map.put("size", pages.size());

        Map<String, Object> maps = new HashMap<>();
        maps.put("tree", map);
        return new ModelAndView("sys/sysdict/chooseDictValue", "dictModel", maps);
    }
}
