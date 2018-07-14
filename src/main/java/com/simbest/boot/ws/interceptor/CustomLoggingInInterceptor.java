/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.ws.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.io.InputStream;

/**
 * 用途：拦截输入的请求SOAP消息
 * 作者: lishuyi
 * 时间: 2018/6/26  16:05
 */
@Slf4j
public class CustomLoggingInInterceptor extends LoggingInInterceptor {


    public CustomLoggingInInterceptor() {
        super(Phase.PRE_STREAM);
        this.setPrettyLogging(true);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        InputStream is = message.getContent(InputStream.class);
        CachedOutputStream os = new CachedOutputStream();
        try {
            IOUtils.copy(is, os);
            os.flush();
            message.setContent(InputStream.class, os.getInputStream());
            is.close();
            String soapXml = IOUtils.toString(os.getInputStream());
            log.debug(soapXml);
            super.handleMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
