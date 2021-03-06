/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums.web.app;

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.uums.api.app.UumsSysAppGroupApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <strong>Title : SysAppController</strong><br>
 * <strong>Description : </strong><br>
 * <strong>Create on : 2018/5/26/026</strong><br>
 * <strong>Modify on : 2018/5/26/026</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LM liumeng@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 */

@Api (description = "应用决策群组相关接口" )
@RestController
@RequestMapping(value = {"/uums/sys/appGroup", "/sys/uums/sys/appGroup"})
public class UumsSysAppGroupController {

    @Autowired
    private UumsSysAppGroupApi uumsSysAppGroupApi;

    /**
     *根据id查看应用决策群组信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查看应用决策群组信息", notes = "根据id查看应用决策群组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用code", dataType = "String", paramType = "query")
    })
    @PostMapping("/findById")
    public JsonResponse findById(@RequestParam String id,@RequestParam  String appcode) {
        return JsonResponse.success(uumsSysAppGroupApi.findById( id,appcode ));
    }

    /**
     *获取权限信息列表并分页
     * @param page
     * @param size
     * @param direction
     * @param appcode
     * @param map
     * @return
     */
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
    @PostMapping(value = "/findAll")
    public JsonResponse findAll( @RequestParam(required = true, defaultValue = "1") int page, //
                                 @RequestParam(required = true, defaultValue = "10") int size, //
                                 @RequestParam(required = true) String direction,
                                 @RequestParam(required = true) String properties,
                                 @RequestParam String appcode,
                                 @RequestBody Map map) {
        return JsonResponse.success(uumsSysAppGroupApi.findAll(page, size,direction,properties,appcode,map));
    }

    /**
     * 根据用户名以及应用code来查看此用户是否拥有使用应用的权限
     * @param username
     * @param appcode
     * @return
     */
    @ApiOperation(value = "根据用户名以及应用code来查看此用户是否拥有使用应用的权限", notes = "根据用户名以及应用code来查看此用户是否拥有使用应用的权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", //
                    paramType = "query") //
    })
    @PostMapping(value = "/ifHasPermission")
    public JsonResponse ifHasPermission( @RequestParam String username, @RequestParam String appcode)  {
        return JsonResponse.success(uumsSysAppGroupApi.ifHasPermission( username,appcode));
    }

}


