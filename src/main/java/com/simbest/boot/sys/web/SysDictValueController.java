/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.google.common.collect.Maps;
import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.sys.model.SysDict;
import com.simbest.boot.sys.model.SysDictValue;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.sys.service.ISysDictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用途：数据字典值控制器
 * 作者: zlxtk
 * 时间: 2018/2/23  10:14
 */
@RestController
@RequestMapping("/sys/dictValue")
public class SysDictValueController {

    @Autowired
    private ISysDictValueService dictValueService;

    @Autowired
    private ISysDictService dictService;


    /**
     * @param page
     * @param size
     * @param searchCode
     * @param id         dictId
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size, //
                             @RequestParam(required = false, defaultValue = "") String searchCode,
                             @RequestParam Long id) {

        SysDict dict = dictService.findById(id);
        if (dict == null) {
            return new ModelAndView("error", "message", "do not find dict by id:" + id);
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("searchCode", searchCode);

        Condition c = new Condition();
        c.eq("dictId", id);
        c.eq("removed", false);
        c.like("name", "%" + searchCode + "%");

        // 生成排序规则
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(page - 1, size, sort);

        Specification<SysDictValue> s = dictValueService.getSpecification(c);
        Page allData = dictValueService.findAll(s, pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("totalSize", allData.getTotalElements());
        map.put("totalPage", allData.getTotalPages());
        map.put("dataList", allData.getContent());
        map.put("dict", dict);
        map.put("size", allData.getSize());
        map.put("page", page);
        map.put("title", "数据字典值");
        map.put("searchD", dataMap);

        return new ModelAndView("sys/sysdict/dictionaryList", "dictModel", map);
    }


    @RequestMapping(value = "/queryByDict")
    @ResponseBody
    public Map<String, Object> queryByDict(Long dictId) {
        Map<String, Object> result = Maps.newHashMap();
        List<SysDictValue> list = dictValueService.findByDictId(dictId);

        result.put("iTotalRecords", list.size());
        result.put("iTotalDisplayRecords", list.size());
        result.put("aaData", list);
        return result;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse create(@RequestBody SysDictValue dictValue) {
        if (dictValue.getId() != null) {
            SysDictValue selectDictValue = dictValueService.findById(dictValue.getId());
            if (selectDictValue == null) {
                return JsonResponse.defaultErrorResponse();
            }
            dictValue.setParentId(selectDictValue.getParentId());
            dictValue.setId(null);
        }
        SysDictValue newSysDictValue = dictValueService.save(dictValue);
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/createChild", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse createChild(@RequestBody SysDictValue dictValue) {
        if (dictValue == null) {
            return JsonResponse.defaultErrorResponse();
        }
        dictValue.setParentId(dictValue.getId());
        dictValue.setId(null);
        SysDictValue newSysDictValue = dictValueService.save(dictValue);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * @param dictValue
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse update(@RequestBody SysDictValue dictValue) {
        SysDictValue newSysDictValue = dictValueService.findById(dictValue.getId());
        if (dictValue == null || newSysDictValue == null) {
            return JsonResponse.defaultErrorResponse();
        }
        newSysDictValue = dictValueService.save(dictValue);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse delete(@RequestBody long id) {
        SysDictValue newSysDictValue = dictValueService.findById(id);
        if (newSysDictValue == null) {
            return JsonResponse.defaultErrorResponse();
        }
        dictValueService.deleteById(id);
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse deleteList(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            SysDictValue newSysDictValue = dictValueService.findById(id);
            if (newSysDictValue == null) {
                continue;
            }
            dictValueService.deleteById(id);
        }
        return JsonResponse.defaultSuccessResponse();
    }

    /**
     * 启用、停用
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/updateEnable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse updateEnable(@RequestBody Map<String, Object> params) {
        Long id = Long.parseLong(params.get("id").toString());
        Boolean enabled = (Boolean) params.get("enabled");
        int state = dictValueService.updateEnable(enabled, id);
        if (state < 1) {
            return JsonResponse.defaultErrorResponse();
        }
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @GetMapping(value = "/getById")
    public ModelAndView getById(@RequestParam(defaultValue = "-1") final long id, @RequestParam long dictId) {
        SysDictValue dictValue;
        if (id == -1) {
            dictValue = new SysDictValue();
        } else {
            dictValue = dictValueService.findById(id);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("dictValue", dictValue);
        map.put("dictId", dictId);
        return new ModelAndView("sys/sysdict/dictionaryForm", "dictModel", map);
    }

    /**
     * 根据dictid获取字段的值
     *
     * @param dictId
     * @return json
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/json/listByDictId", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResponse listJson(@RequestParam(required = false, defaultValue = "1") int page, //
                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                 @RequestParam(required = false, defaultValue = "-1") int dictId, //
                                 @RequestParam(required = false, defaultValue = "") String name) {

        // 获取查询条件
        Condition condition = new Condition();
        condition.eq("enabled", true);
        condition.eq("removed", false);
        condition.eq("dictId", dictId);
        condition.like("name", "%" + name + "%");

        // 生成排序规则
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = dictValueService.getPageable(page, size, "ASC", "id");

        Specification<SysDictValue> s = dictValueService.getSpecification(condition);

        // 获取查询结果
        Page pages = dictValueService.findAll(s, pageable);

        // 构成返回信息
        Map<String, Object> searchD = new HashMap<>();
        searchD.put("dictId", dictId);
        searchD.put("name", name);

        Map<String, Object> map = new HashMap<>();
        map.put("totalSize", pages.getTotalElements());
        map.put("totalPage", pages.getTotalPages());
        map.put("list", pages.getContent());
        map.put("size", pages.getSize());
        map.put("page", page);
        map.put("searchD", searchD);

        JsonResponse res = JsonResponse.builder() //
                .errcode(JsonResponse.SUCCESS_CODE) //
                .message("查询成功！") //
                .build();

        res.setData(map);
        return res;
    }

}
