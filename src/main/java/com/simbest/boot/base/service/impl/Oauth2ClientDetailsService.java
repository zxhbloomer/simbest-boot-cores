package com.simbest.boot.base.service.impl;

import com.simbest.boot.base.model.Oauth2ClientDetails;
import com.simbest.boot.base.repository.Oauth2ClientDetailsRepository;
import com.simbest.boot.base.service.IOauth2ClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * 用途：Oauth2 客户端信息逻辑层
 * 作者: lishuyi
 * 时间: 2018/2/23  10:14
 */
@Slf4j
@Service
public class Oauth2ClientDetailsService extends GenericService<Oauth2ClientDetails, String> implements IOauth2ClientDetailsService {

    @Autowired
    private Oauth2ClientDetailsRepository repository;

    @Autowired
    public Oauth2ClientDetailsService(Oauth2ClientDetailsRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return repository.findByClientId(clientId);
    }
}
