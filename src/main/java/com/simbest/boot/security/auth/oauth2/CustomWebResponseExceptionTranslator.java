/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.oauth2;

import com.simbest.boot.base.exception.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/8/29  21:28
 */
@Component
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        Exceptions.printException(e);
        if(e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOauthException(oAuth2Exception.getMessage()));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new OAuth2Exception(e.getMessage()));
        }
    }
}
