package com.simbest.boot.base.web.controller;

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ErrorCodeConstants;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <strong>Title</strong> : 登录控制器<br>
 * <strong>Description</strong> : <br>
 * <strong>Create on</strong> : 2018/02/27<br>
 * <strong>Modify on</strong> : 2018/03/02<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@Api(description = "登录控制器")
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authManager;

    /**
     * @return login
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public String login() {
        return "login";
    }

    @PostMapping("submit")
    public JsonResponse submit(@RequestParam String username, @RequestParam String password,
                               @RequestParam String validateCode) {
        try {
            UsernamePasswordAuthenticationToken authRequest
                    = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return JsonResponse.defaultSuccessResponse();
        } catch (Exception e) {
            JsonResponse response = JsonResponse.defaultErrorResponse();
            if (e != null) {
                if (e instanceof BadCredentialsException) {
                    response.setErrmsg(ErrorCodeConstants.LOGIN_ERROR_BAD_CREDENTIALS);
                } else if (e instanceof InternalAuthenticationServiceException) {
                    response.setErrmsg(ErrorCodeConstants.USERNAME_NOT_FOUND);
                }
            }
            return response;
        }
    }

    /**
     * @return 403
     */
    @RequestMapping(value = "/403", method = {RequestMethod.POST, RequestMethod.GET})
    public String accesssDenied() {
        return "403";
    }
}
