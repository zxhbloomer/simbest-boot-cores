/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums.api.group;

import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
@Component
@Slf4j
public class UumsSysGroupApi {
    private static final String USER_MAPPING = "/action/group/group/";
    private static final String SSO = "/sso";
    @Value ("${app.uums.address}")
    private String uumsAddress;
    //private String uumsAddress="http://localhost:8080/uums";
    @Autowired
    private RsaEncryptor encryptor;

    /**
     * 根据id查找
     * @param id
     * @param appcode
     * @return
     */
    public JsonResponse findById(Long id,String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findById"+SSO)
                .param( AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param( AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("id", String.valueOf(id))
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     * 单表条件查询
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param sysAppDecisionMap
     * @return
     */
    public JsonResponse findAll( int page, int size, String direction,String properties,String appcode,Map sysAppDecisionMap) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findAll"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( json0 )
                .asBean(JsonResponse.class );
        return response;
    }

    /**
     * 根据sid查看父群组信息
     * @param appcode
     * @param sid
     * @return
     */
    public JsonResponse findParentGroup(String appcode,String sid) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findParentGroup"+SSO)
                .param( AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param( AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("sid", String.valueOf(sid))
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     *查看一个群组下的全部子群组
     * @param appcode
     * @param sid
     * @return
     */
    public JsonResponse findAllSonGroup(String appcode,String sid) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findAllSonGroup"+SSO)
                .param( AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param( AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("sid", String.valueOf(sid))
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     *查看某个应用下群组的信息并分页
     * @param appcode
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param sysAppDecisionMap
     * @return
     */
    public JsonResponse findGroupByApp( String appcode,int page,int size,String direction,String properties, Map sysAppDecisionMap) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findGroupByApp"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( json0 )
                .asBean(JsonResponse.class );
        return response;
    }

    /**
     *查看某个用户所属群组的信息并分页
     * @param appcode
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param sysUserGroupMap
     * @return
     */
    public JsonResponse findGroupByUsername( String appcode,int page,int size,String direction,String properties,Map sysUserGroupMap) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysUserGroupMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findGroupByUsername"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( json0 )
                .asBean(JsonResponse.class );
        return response;
    }

    /**
     *根据应用编码查询拥有改应用编码合法权限的群组信息
     * @param appcode
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @return
     */
    public JsonResponse findGroupByPermission(String appcode,int page,int size,String direction,String properties) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findGroupByPermission"+SSO)
                .param( AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param( AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("appCode", appcode)
                .asBean(JsonResponse.class);
        return response;
    }
}
