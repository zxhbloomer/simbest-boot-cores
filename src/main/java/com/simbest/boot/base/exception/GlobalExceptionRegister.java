/**
 * 版权所有 © 北京晟壁科技有限公司 2017-2027。保留一切权利!
 */
package com.simbest.boot.base.exception;


import com.google.common.collect.Maps;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ErrorCodeConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.multipart.MultipartException;

import java.util.Map;

/**
 * 用途：系统中注册对应返回信息
 * 作者: lishuyi
 * 时间: 2017/11/4  15:46
 */
public final class GlobalExceptionRegister {
    private static Map<Class<? extends Exception>, JsonResponse> errorMap = Maps.newHashMap();

    public static void main(String[] args) {
        HttpStatus a = HttpStatus.BAD_REQUEST;
        System.out.println(a.name());
        System.out.println(a.value());
    }

    //初始化状态码与文字说明
    static {
        errorMap.put(Exception.class,
                JsonResponse.builder().errcode(HttpStatus.BAD_REQUEST.value()).errmsg(HttpStatus.BAD_REQUEST.name()).build());
        errorMap.put(RuntimeException.class,
                JsonResponse.builder().errcode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errmsg(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .build());
        errorMap.put(AccessDeniedException.class,
                JsonResponse.builder().errcode(HttpStatus.UNAUTHORIZED.value()).errmsg(HttpStatus.UNAUTHORIZED.name()).build());
        errorMap.put(AccessDeniedException.class,
                JsonResponse.builder().errcode(HttpStatus.FORBIDDEN.value()).errmsg(HttpStatus.FORBIDDEN.name()).build());
        errorMap.put(HttpRequestMethodNotSupportedException.class,
                JsonResponse.builder().errcode(HttpStatus.METHOD_NOT_ALLOWED.value()).errmsg(HttpStatus.METHOD_NOT_ALLOWED.name())
                        .build());

        errorMap.put(MultipartException.class,
                JsonResponse.builder().errcode(ErrorCodeConstants.ATTACHMENT_SIZE_EXCEEDS).errmsg("Attachment size exceeds the allowable limit!")
                        .build());

//
//        errorMap.put("800", "参数不合法");
//        errorMap.put("801", "无效的Token");
//        errorMap.put("802", "解密失败,密文数据已损坏");
//        errorMap.put("803", "请重新登录");
//
//        errorMap.put("1000", "[服务器]运行时异常");
//        errorMap.put("1001", "[服务器]空值异常");
//        errorMap.put("1002", "[服务器]数据类型转换异常");
//        errorMap.put("1003", "[服务器]IO异常");
//        errorMap.put("1004", "[服务器]未知方法异常");
//        errorMap.put("1005", "[服务器]数组越界异常");
//        errorMap.put("1006", "[服务器]网络异常");
//
//        errorMap.put("1010", "数据已失效");
//        errorMap.put("1011", "数据违法唯一性约束");
//        errorMap.put("1012", "无数据返回");
//        errorMap.put("1013", "多条数据返回");
//        errorMap.put("1014", "缺少参数或值为空");
//        errorMap.put("1014", "参数格式不合法");
//
//        errorMap.put("1020", "用户未注册");
//        errorMap.put("1021", "用户已注册");
//        errorMap.put("1022", "用户未绑定");
//        errorMap.put("1023", "用户已失效");
//        errorMap.put("1024", "用户名或密码错误");
//        errorMap.put("1025", "用户帐号冻结");
//
//        errorMap.put("1020", "验证码发送失败");
//        errorMap.put("1021", "验证码失效");
//        errorMap.put("1022", "验证码错误");
//        errorMap.put("1023", "验证码不可用");
//        errorMap.put("1024", "短信平台异常");
//
//        errorMap.put("1030", "邮件发送错误");
//        errorMap.put("1031", "邮箱地址不存在");
//        errorMap.put("1032", "邮箱地址格式错误");
//        errorMap.put("1033", "手机号码不存在");
//        errorMap.put("1034", "手机号码格式错误");
//        errorMap.put("1035", "电话号码不存在");
//        errorMap.put("1036", "电话号码格式错误");
    }

    private GlobalExceptionRegister() {
    }

    /**
     * @param clazz 异常类
     * @param data  额外返回信息
     * @return 返回JsonResponse
     */
    public static JsonResponse returnErrorResponse(Exception e) {
        Exceptions.printException(e);
        JsonResponse response = errorMap.get(e.getClass());
        if (response == null) {
            response = JsonResponse.defaultErrorResponse();
        }
        response.setData(e.getMessage());
        return response;
    }
}
