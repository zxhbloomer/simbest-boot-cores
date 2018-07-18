/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.simbest.boot.base.web.controller.LogicController;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.sys.model.SysDictValue;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.sys.service.ISysDictValueService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 用途：数据字典值控制器
 * 作者: zlxtk
 * 时间: 2018/2/23  10:14
 */
@RestController
@RequestMapping("/sys/dictValue")
public class SysDictValueController extends LogicController<SysDictValue,Integer>{

    private ISysDictValueService sysDictValueService;

    @Autowired
    private ISysDictService dictService;

    @Autowired
    public SysDictValueController(ISysDictValueService sysDictValueService) {
        super(sysDictValueService);
        this.sysDictValueService=sysDictValueService;
    }

    /**
     * 新增一个字典值
     * @param sysDictValue
     * @return
     */
    //设置权限，后面再开启
    //@PreAuthorize ("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "新增一个字典值", notes = "新增一个字典值")
    public JsonResponse create(@RequestBody(required = false) SysDictValue sysDictValue) {
        return super.create( sysDictValue );
    }

    /**
     * 修改一个字典值
     * @param sysDictValue
     * @return
     */
    //设置权限，后面再开启
    //@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "修改一个字典值", notes = "修改一个字典值")
    public JsonResponse update( @RequestBody(required = false) SysDictValue sysDictValue) {
        return super.update(sysDictValue );
    }

    /**
     * 根据id逻辑删除
     * @param id
     * @return
     */
    //@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @ApiOperation(value = "根据id删除字典值", notes = "根据id删除字典值")
    @ApiImplicitParam (name = "id", value = "字典值ID",  dataType = "Integer", paramType = "query")
    public JsonResponse deleteById(@RequestParam(required = false) Integer id) {
        return super.deleteById( id );
    }

    /**
     * 先修改再逻辑删除字典值
     * @param sysDictValue
     * @return
     */
    @ApiOperation(value = "先修改再逻辑删除字典值", notes = "先修改再逻辑删除字典值")
    public JsonResponse delete(@RequestBody(required = false) SysDictValue sysDictValue) {
        return super.delete(sysDictValue);
    }

    /**
     * 批量逻辑删除字典值
     * @param ids
     * @return JsonResponse
     */
    //@PreAuthorize("hasAuthority('ROLE_SUPER')")  // 指定角色权限才能操作方法
    @ApiOperation(value = "批量逻辑删除字典值", notes = "批量逻辑删除字典值")
    @ApiImplicitParam(name = "ids", value = "字典类型ID", dataType = "Set<Integer>", paramType = "query")
    public JsonResponse deleteAllByIds(@RequestBody(required = false) Integer[] ids) {
        return  super.deleteAllByIds(ids);
    }

    /**
     *修改可见
     * @param id
     * @param enabled
     * @return
     */
    @ApiOperation(value = "修改可见", notes = "修改可见")
    @ApiImplicitParams ({@ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "enabled", value = "是否可用", required = true, dataType = "Boolean", paramType = "query")
    })
    public JsonResponse updateEnable(@RequestParam(required = false) Integer id, @RequestParam(required = false) Boolean enabled) {
        return  super.updateEnable( id,enabled );
    }

    //批量修改可见

    /**
     *根据id查询字典值
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询字典值", notes = "根据id查询字典值")
    @ApiImplicitParam(name = "id", value = "字典类型ID", dataType = "Integer", paramType = "query")
    @PostMapping(value = {"/findById","/findById/sso"})
    public JsonResponse findById(@RequestParam(required = false) Integer id) {
        return super.findById( id );
    }

    /**
     *获取字典值信息列表并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param sysDictValue
     * @return
     */
    @ApiOperation(value = "获取字典值信息列表并分页", notes = "获取字典值信息列表并分页")
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
    @PostMapping(value = {"/findAll","/findAll/sso"})
    public JsonResponse findAll( @RequestParam(required = false, defaultValue = "1") int page, //
                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                 @RequestParam(required = false) String direction, //
                                 @RequestParam(required = false) String properties, //
                                 @RequestBody(required = false) SysDictValue sysDictValue //
    ) {
        return super.findAll( page,size,direction, properties,sysDictValue);
    }

    /*@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size, //
                             @RequestParam(required = false, defaultValue = "") String searchCode,
                             @RequestParam Integer id) {

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

        Specification<SysDictValue> s = sysDictValueService.getSpecification(c);
        Page allData = sysDictValueService.findAll(s, pageable);

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
    }*/

    /**
     * 新增子字典值
     * @param dictValue
     * @return
     */
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @PostMapping(value = "/createChild")
    @ResponseBody
    public JsonResponse createChild(@RequestBody(required = false) SysDictValue dictValue) {
        if (dictValue == null) {
            return JsonResponse.defaultErrorResponse();
        }
        dictValue.setParentId(dictValue.getId());
        dictValue.setId(null);
        SysDictValue newSysDictValue = sysDictValueService.save(dictValue);
        return JsonResponse.defaultSuccessResponse();
    }


   /* @PreAuthorize("hasAuthority('ROLE_USER')")  // 指定角色权限才能操作方法
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
        Pageable pageable = sysDictValueService.getPageable(page, size, "ASC", "id");

        Specification<SysDictValue> s = sysDictValueService.getSpecification(condition);

        // 获取查询结果
        Page pages = sysDictValueService.findAll(s, pageable);

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
    }*/

    /**
     * 根据字典类型以及上级数据字典值id查询数据字典中相应值的name以及value的值
     * @param sysDictValue
     * @return
     */
    @ApiOperation (value = "根据字典类型以及上级数据字典值id查询数据字典中相应值的name以及value的值", notes = "根据字典类型以及上级数据字典值id查询数据字典中相应值的name以及value的值")
    @PostMapping(value = "/findDictValue")
    public JsonResponse findDictValue(@RequestBody(required = false) SysDictValue sysDictValue){
        return JsonResponse.success(sysDictValueService.findDictValue(sysDictValue));
    }

    /**
     *查看数据字典的所有值
     * @return
     */
    @ApiOperation(value = "查看数据字典的所有值", notes = "查看数据字典的所有值")
    @PostMapping(value = "/findAllDictValue")
    public JsonResponse findAllDictValue(){
        return JsonResponse.success(sysDictValueService.findAllDictValue());
    }

}
