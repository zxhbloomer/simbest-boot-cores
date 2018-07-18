/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.ws.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.io.OutputStream;

/**
 * 用途：拦截输出的请求SOAP消息
 * 作者: lishuyi
 * 时间: 2018/6/26  16:06
 */
@Slf4j
public class SoapLoggingOutInterceptor extends LoggingOutInterceptor {

    public SoapLoggingOutInterceptor() {
        super(Phase.PRE_STREAM);
        this.setPrettyLogging(true);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        OutputStream out = message.getContent(OutputStream.class);
        final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(out);
        message.setContent(OutputStream.class, newOut);
        newOut.registerCallback(new LoggingCallback());
        super.handleMessage(message);
    }

    public class LoggingCallback implements CachedOutputStreamCallback {
        public void onFlush(CachedOutputStream cos) {
        }

        public void onClose(CachedOutputStream cos) {
            try {
                StringBuilder builder = new StringBuilder();
                cos.writeCacheTo(builder, limit);
                String soapXml = builder.toString();
                log.debug(soapXml);
            } catch (Exception e) {
            }
        }
    }
}
