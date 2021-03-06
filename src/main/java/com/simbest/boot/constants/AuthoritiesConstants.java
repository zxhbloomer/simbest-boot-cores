/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.constants;

/**
 * 用途：定义权限相关常量
 * 作者: lishuyi
 * 时间: 2018/2/6  17:11
 */
public class AuthoritiesConstants {
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLE = "role";

    public static final String PERMISSION = "permission";

    public static final String SSO_UUMS_USERNAME = "username";

    public static final String SSO_UUMS_PASSWORD = "password";

    public static final String SSO_API_KEYWORD = "keyword";

    public static final String SSO_API_KEYTYPE = "keytype";

    public static final String SSO_API_USERNAME = "loginuser";

    public static final String SSO_API_APP_CODE = "appcode";

    public static final String SSO_API_UID = "uid";

    public static final int PASSWORD_SALT_LENGTH = 12;

    public static final int ATTEMPT_LOGIN_INIT_TIMES = 1;
    public static final int ATTEMPT_LOGIN_MAX_TIMES = 5;
    public static final int ATTEMPT_LOGIN_FAILED_WAIT_SECONDS = 60 * 5;
    public static final String LOGIN_FAILED_KEY = "LOGIN_FAILED:";

}
