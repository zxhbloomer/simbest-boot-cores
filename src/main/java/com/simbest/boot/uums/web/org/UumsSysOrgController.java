/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums.web.org;

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.uums.api.org.UumsSysOrgApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

@Api (description = "系统组织操作相关接口" )
@RestController
@RequestMapping("/uums/sys/org")
public class UumsSysOrgController {

    @Autowired
    private UumsSysOrgApi uumsSysOrgApi;

    /**
     * 查看某个父组织的子组织
     * @param appcode
     * @param orgCode
     * @return
     */
    @ApiOperation (value = "页面初始化时获取根组织以及根组织下一级组织", notes = "页面初始化时获取根组织以及根组织下一级组织")
    @ApiImplicitParams( {
            @ApiImplicitParam(name = "appcode", value = "应用code", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orgCode", value = "组织code", dataType = "String", paramType = "query")
    } )
    @PostMapping ("/findSonByParentOrgId")
    public JsonResponse findSonByParentOrgId( @RequestParam  String appcode,@RequestParam String orgCode) {
        return JsonResponse.success(uumsSysOrgApi.findSonByParentOrgId( appcode,orgCode ));
    }

    /**
     *页面初始化时获取根组织以及根组织下一级组织
     * @param appcode
     * @return
     */
    @ApiOperation (value = "页面初始化时获取根组织以及根组织下一级组织", notes = "页面初始化时获取根组织以及根组织下一级组织")
    @ApiImplicitParam(name = "appcode", value = "应用code", dataType = "String", paramType = "query")
    @PostMapping ("/findRootAndNextRoot")
    public JsonResponse findRootAndNextRoot( @RequestParam  String appcode) {
        return JsonResponse.success(uumsSysOrgApi.findRootAndNext( appcode ));
    }


}


