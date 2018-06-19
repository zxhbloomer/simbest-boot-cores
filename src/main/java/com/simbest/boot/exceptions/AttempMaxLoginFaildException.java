/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 用途：尝试登录超过最大次数异常
 * 作者: lishuyi
 * 时间: 2018/6/7  20:03
 */
public class AttempMaxLoginFaildException extends AuthenticationException {

    /**
     * Constructs an <code>InsertExistObjectException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public AttempMaxLoginFaildException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>InsertExistObjectException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public AttempMaxLoginFaildException(String msg, Throwable t) {
        super(msg, t);
    }
}
