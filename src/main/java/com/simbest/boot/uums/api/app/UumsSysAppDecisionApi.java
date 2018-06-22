/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums.api.app;

import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.security.ISysAppDecision;
import com.simbest.boot.security.SimpleSysAppDecision;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UumsSysAppDecisionApi {
    private static final String USER_MAPPING = "/action/app/decision/";
    private static final String SSO = "/sso";
    /*@Value ("${app.uums.address}")
    private String uumsAddress;*/
    private String uumsAddress="http://localhost:8080/uums";
    @Autowired
    private RsaEncryptor encryptor;

    /**
     * 根据id查找
     * @param id
     * @param appcode
     * @return
     */
    public ISysAppDecision findById( Long id, String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(this.uumsAddress + USER_MAPPING + "findById"+SSO)
                .param( AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param( AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("id", String.valueOf(id))
                .asBean(JsonResponse.class);
        String json = JacksonUtils.obj2json(response.getData());
        ISysAppDecision auth = JacksonUtils.json2obj(json, SimpleSysAppDecision.class);
        return auth;
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
     * 根据appcode以及其下的流程id及活动id获取其下全部决策信息
     * @param appcode
     * @param sysAppDecisionMap
     * @return
     */
    public List<ISysAppDecision> findDecisions(String appcode,Map sysAppDecisionMap) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(this.uumsAddress + USER_MAPPING + "findDecisions"+SSO+"?loginuser="+username2+"&appcode="+appcode)
                .json( json0 )
                .asBean(JsonResponse.class );
        List<Object> appDecisions=(ArrayList<Object> )response.getData();
        List<ISysAppDecision> appDecisionList=new ArrayList<>(  );
        for(Object appDecision:appDecisions){
            String json = JacksonUtils.obj2json(appDecision);
            ISysAppDecision auth = JacksonUtils.json2obj(json, SimpleSysAppDecision.class);
            appDecisionList.add(auth);
        }
        return appDecisionList;
    }
}
