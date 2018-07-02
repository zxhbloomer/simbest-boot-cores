/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */

package com.simbest.boot.uums.api.user;

import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.security.IPermission;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.SimplePermission;
import com.simbest.boot.security.SimpleUser;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
public class UumsSysUserinfoApi {
    private static final String USER_MAPPING = "/action/user/user/";
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

    public IUser findById(Long id, String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findById"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("id", String.valueOf(id))
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        IUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
        return auth;
    }


    /**
     * 单表条件查询并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param sysUserinfoMap
     * @return
     */
    public JsonResponse findAll(int page, int size,String direction,String properties,String appcode, Map sysUserinfoMap ) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysUserinfoMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findAll"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( json0 )
                .asBean(JsonResponse.class );
        return response;
    }

    /**
     * 获取全部用户信息列表并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @return
     */
    public JsonResponse findAllUsers(int page, int size,String direction,String properties,String appcode) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findAllUsers"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .asBean(JsonResponse.class );
        return response;
    }

    /**
     * 根据用户名查询用户信息(BPS专用)
     * @param username
     * @param appcode
     * @return
     */
    public IUser findByUsername(String username,String appcode) {
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findByUsername"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME,encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("username",username)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        IUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
        return auth;
    }


    /**
     * 根据群组sid查询用户信息并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param groupSid
     * @return
     */

    public JsonResponse findUserByGroup(int page,  int size, String direction,  String properties,String appcode, Long groupSid ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findUserByGroup"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("id", String.valueOf(groupSid))
                .asBean(JsonResponse.class);
        return response;
    }


    /**
     * 根据组织orgcode获取用户并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param orgCode
     * @return
     */

    public JsonResponse findUserByOrg(int page,  int size,  String direction,  String properties,String appcode, String orgCode ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findUserByOrg"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("orgCode", orgCode)
                .asBean(JsonResponse.class);
        return response;
    }


    /**
     * 根据职位名获取用户并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param positionName
     * @return
     */
    public JsonResponse findUserByPosition( int page, int size, String direction, String properties,String appcode, String positionName ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findUserByPosition"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("positionName", positionName)
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     * 根据角色id获取用户但不分页
     * @param roleId
     * @return
     */
    public List<IUser> findUserByRoleNoPage(Integer roleId ,String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findUserByRoleNoPage"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("roleId", String.valueOf(roleId))
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        ArrayList<Object> users=(ArrayList<Object>)response.getData();
        List<IUser> authList=new ArrayList<>();
        for( Object user:users){
            String json = JacksonUtils.obj2json(response.getData());
            IUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
            authList.add(auth);
        }
        return authList;
    }

    /**
     * 根据角色id获取用户并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param roleId
     * @return
     */
    public JsonResponse findUserByRole(int page, int size, String direction,  String properties,String appcode,Integer roleId ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findUserByRole"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("roleId", String.valueOf(roleId))
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     * 查询某个人以及当前应用下的全部权限
     * @param appcode
     * @param username
     * @return
     */
    public List<IPermission> findUserAppPermission( String username, String appcode) {
        String username1 = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username1);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findPositionByUsername"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username1))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("username",username)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        List<Object> sysPermissionList=(ArrayList<Object>)response.getData();
        List<IPermission> permissionList=new ArrayList<>(  );
        for(Object sysPermission:sysPermissionList){
            String json = JacksonUtils.obj2json(sysPermission);
            IPermission auth = JacksonUtils.json2obj(json, SimplePermission.class);
            permissionList.add(auth);
        }
        return permissionList;
    }

    /**
     * 根据过滤条件获取决策下的用户
     * @param appcode
     * @param sysAppDecisionmap
     * @return
     */
    public List<IUser> findUserByDecisionNoPage(String appcode,Map sysAppDecisionmap){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        //sysAppDecisionmap.put("spare2",username);
        String json0=JacksonUtils.obj2json(sysAppDecisionmap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findUserByDecisionNoPage"+SSO+"?loginuser="+username2+"&appcode="+appcode
        +"&username="+username)
                .json( json0 )
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        List<Object> users=(ArrayList<Object>)response.getData();
        List<IUser> userList=new ArrayList<>(  );
        for(Object user:users){
            String json = JacksonUtils.obj2json(user);
            IUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
            userList.add(auth);
        }
        return userList;
    }


    /*
    public JsonResponse findUserByApp(@RequestParam int page,@RequestParam int size, @RequestParam String direction,@RequestParam String properties,@RequestParam String appcode, Map sysAppDecisionMap){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findUserByApp"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( js
    }on0 ).
                        asBean(JsonResponse.class );
        return response;
    }

    //查询多个应用下参与的全部用户


    public JsonResponse findUserByAppNoPage(String appcode, Map sysAppDecisionMap){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findUserByAppNoPage"+SSO+"?loginuser="+username2+"&appcode="+appcode)
                .json( json0 ).
                        asBean(JsonResponse.class);
        return response;
    }

    public JsonResponse findUserByAllAppNoPage(@RequestParam String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findUserByAllAppNoPage"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .asBean(JsonResponse.class);
        return response;
    }


    public JsonResponse findUserByAppPermission( @RequestParam(required = false, defaultValue = "1") int page, //
                                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                                 @RequestParam(required = false) String direction, //
                                                 @RequestParam(required = false) String properties,
                                                 @RequestParam String appcode,
                                                 List<Map> sysAppGroups){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppGroups);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findUserByAppPermission"+SSO+"?loginuser="+username2+"&appcode="+appcode)
                .json( json0 )
                .asBean(JsonResponse.class);
        return response;
    }*/

}

