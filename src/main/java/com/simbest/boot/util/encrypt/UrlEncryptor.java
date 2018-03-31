/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.encrypt;

import com.simbest.boot.base.exception.Exceptions;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 用途：对URL进行编码解密工具类
 * 作者: lishuyi 
 * 时间: 2017/12/29  17:16 
 */
@Component
public class UrlEncryptor extends AbstractEncryptor {

    /**
     * 加密，编码默认为UTF-8
     * @param source 原值
     * @return 编码
     */
    public String encryptSource(String source) {
        try {
            return URLEncoder.encode(source, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 加密
     * @param source 原值
     * @param encoding 字符集
     * @return 加密值
     */
    public String encrypt(String source, String encoding) {
        try {
            return URLEncoder.encode(source, encoding);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 解密
     * @param code 编码默认为UTF-8
     * @return 解密值
     */
    public String decryptCode(String code) {

        try {
            return URLDecoder.decode(code, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 解密
     * @param code 编码
     * @param encoding 字符集
     * @return 解密值
     */
    public static String decrypt(String code, String encoding) {

        try {
            return URLDecoder.decode(code, encoding);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
