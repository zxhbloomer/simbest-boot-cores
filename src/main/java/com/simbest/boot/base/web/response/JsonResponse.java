/**
 * 版权所有 © 北京晟壁科技有限公司 2017-2027。保留一切权利!
 */
package com.simbest.boot.base.web.response;


import lombok.*;

/**
 * 用途：Restful 接口通用返回的JSON对象
 * 作者: lishuyi 
 * 时间: 2017/11/4  15:43 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class JsonResponse {
    public static final Integer SUCCESS_CODE = 0;

    public static final String SUCCESS_MSG = "ok";

    public static final Integer UNKNOWN_ERROR_CODE = 500;

    public static final String UNKNOWN_ERROR_MSG = "unknown error!";

    //状态码，请求正常返回为0
    @NonNull
    private Integer errcode;

    //提示信息
    private String errmsg;

    //业务数据
    private Object data;

    /**
     *
     * @return 默认成功输出
     */
    public static JsonResponse defaultSuccessResponse() {
        return JsonResponse.builder().errcode(SUCCESS_CODE).errmsg(SUCCESS_MSG).build();
    }

    /**
     *
     * @return 默认失败输出
     */
    public static JsonResponse defaultErrorResponse() {
        return JsonResponse.builder().errcode(UNKNOWN_ERROR_CODE).errmsg(UNKNOWN_ERROR_MSG).build();
    }
}
