/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用途：异常工具类
 * 作者: lishuyi 
 * 时间: 2017/12/28  21:46 
 */
@Slf4j
public class Exceptions {

    /**
     * 将CheckedException转换为UncheckedException
     * @param e Exception
     * @return RuntimeException
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 将ErrorStack转化为String
     * @param e Exception
     * @return 异常堆栈信息
     */
    public static String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起
     * @param ex Exception
     * @param causeExceptionClasses Exception Class
     * @return
     */
    @SafeVarargs // 警告: [unchecked] 参数化 vararg 类型Class<? extends Exception>的堆可能已受污染
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    public static void printException(Exception e) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(buf, true));
        String expMsg = buf.toString();
        log.error(expMsg);
    }


}
