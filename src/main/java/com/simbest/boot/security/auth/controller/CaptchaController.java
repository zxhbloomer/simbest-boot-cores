/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.controller;

import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.util.captcha.Captcha;
import com.simbest.boot.util.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <strong>Title</strong> : 验证码生成器<br>
 * <strong>Description</strong> : <br>
 * <strong>Create on</strong> : 2018/02/27<br>
 * <strong>Modify on</strong> : 2018/03/02<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@Api(description = "CaptchaController", tags = {"权限管理-验证码管理"})
@Slf4j
@RestController
public class CaptchaController {

    /**
     * 自定义验证码
     * 参考： https://www.sojson.com/blog/71.html
     *
     * @param response
     * @param request
     */
    @GetMapping("/captcha")
    public void getJPGCode(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpg");

            /**
             * jgp格式验证码
             * 宽，高，位数。
             */
            Captcha captcha = new SpecCaptcha(146, 33, 4);
            //输出
            captcha.out(request, response.getOutputStream());
        } catch (Exception e) {
            Exceptions.printException(e);
        }
    }
}
