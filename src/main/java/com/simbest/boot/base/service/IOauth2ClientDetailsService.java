package com.simbest.boot.base.service;

import com.simbest.boot.base.model.Oauth2ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * 用途：Oauth2 客户端信息逻辑层
 * 作者: lishuyi
 * 时间: 2018/2/23  10:14
 */
public interface IOauth2ClientDetailsService extends IGenericService<Oauth2ClientDetails, String>, ClientDetailsService {


}
