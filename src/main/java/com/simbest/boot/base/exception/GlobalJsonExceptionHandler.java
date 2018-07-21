/**
 * 版权所有 © 北京晟壁科技有限公司 2017-2027。保留一切权利!
 */
package com.simbest.boot.base.exception;


import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ErrorCodeConstants;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途：全局异常处理，只处理应用内runtime的JSON请求错误，其他错误由com.simbest.exclude.GlobalErrorController处理，包括未定义的页面和JSON请求
 * 作者: lishuyi
 * 时间: 2017/11/4  15:34
 */
@Slf4j
@RestControllerAdvice
public class GlobalJsonExceptionHandler {

    /**
     * @param req Http请求
     * @param e   JsonException
     * @return JsonResponse
     */
    @ExceptionHandler(value = Exception.class)
    public JsonResponse handleErrorException(HttpServletRequest req, Exception e) {
        return handleException(req, e);
    }

    /**
     * @param req Http请求
     * @param e   AccessDeniedException
     * @return JsonResponse
     */
    @ExceptionHandler(value = RuntimeException.class)
    public JsonResponse handleRuntimeException(HttpServletRequest req, RuntimeException e) {
        return handleException(req, e);
    }

    /**
     * 通用异常处理入口
     *
     * @param req Http请求
     * @param e   Exception
     * @return JsonResponse
     */
    private JsonResponse handleException(HttpServletRequest req, Exception e) {
        log.warn(req.getRequestURL().toString());
        Exceptions.printException(e);
        JsonResponse response = GlobalExceptionRegister.returnErrorResponse(e);
        response.setTimestamp(DateUtil.getCurrent());
        response.setPath(req.getRequestURL().toString());
        return response;
    }

    /**
     * 专门处理文件上传异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<?> handleMultipartException(HttpServletRequest req, RuntimeException e) {
        JsonResponse response = JsonResponse.builder().errcode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name()).message("Upload attachment failed-上传文件失败").timestamp(DateUtil.getCurrent()).build();
        return new ResponseEntity<>(JacksonUtils.obj2json(response), HttpStatus.BAD_REQUEST);
    }

    /**
     * 专门处理文件上传异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(HttpServletRequest req, RuntimeException e) {
        JsonResponse response = JsonResponse.builder().errcode(HttpStatus.PAYLOAD_TOO_LARGE.value()).status(HttpStatus.PAYLOAD_TOO_LARGE.value())
                .error(HttpStatus.PAYLOAD_TOO_LARGE.name()).message("Attachment size exceeds-文件过大").timestamp(DateUtil.getCurrent()).build();
//        return new ResponseEntity<>(JacksonUtils.obj2json(response), HttpStatus.PAYLOAD_TOO_LARGE);
        return "req";
    }
}
