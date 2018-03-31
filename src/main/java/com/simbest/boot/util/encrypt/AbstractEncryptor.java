/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用途：支持SHA-1/MD5消息摘要的抽象工具类.
 * 作者: lishuyi 
 * 时间: 2017/12/29  16:57 
 */
@Slf4j
public abstract class AbstractEncryptor {
    public static final String DEFAULT_URL_ENCODING = "UTF-8";
    public static final String SHA1 = "SHA-1";
    public static final String MD5 = "MD5";

    protected static void encryptBefore(String part) {
        log.debug("++++++++++encrypt source before value is: " + part);
    }

    protected static void encryptAfter(String part) {
        log.debug("++++++++++encrypt source after value is: " + part);
    }

    protected static void decryptBefore(String part) {
        log.debug("----------decrypt source before value is: " + part);
    }

    protected static void decryptAfter(String part) {
        log.debug("----------decrypt source after value is: " + part);
    }

    public String encrypt(String source) {
        encryptBefore(source);
        String code = encryptSource(source);
        encryptAfter(code);
        return code;
    }

    public String decrypt(String code) {
        decryptBefore(code);
        String value = decryptCode(code);
        decryptAfter(value);
        return value;
    }

    protected abstract String encryptSource(String source);

    protected abstract String decryptCode(String code);

    /**
     * 根据算法对目标字符串加密
     * @param source 目标字符串
     * @param algorithm 算法
     * @return 加密字符串
     * @throws NoSuchAlgorithmException
     */
    protected static final String encryptAlgorithm(String source, String algorithm) {
        encryptBefore(source);
        if (source == null) {
            source = "";
        }
        String result = "";
        byte[] resByteArray = null;
        try {
            resByteArray = encryptAlgorithm(source.getBytes(), algorithm);
        } catch (NoSuchAlgorithmException ex) {
            // this should never happen
            throw new RuntimeException(ex);
        }
        result = toHexString(resByteArray);
        encryptAfter(result);
        return result;
    }

    /**
     * 根据算法对目标字符串加密
     * @param source 目标字符串byte流
     * @param algorithm 算法
     * @return 加密字符串
     * @throws NoSuchAlgorithmException
     */
    protected static final byte[] encryptAlgorithm(byte[] source, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        md.update(source);
        return md.digest();
    }

    /**
     *
     * @param hash hash字节流
     * @return 加密字符串
     */
    protected static final String toHexString(final byte[] hash) {
        StringBuffer sb = new StringBuffer(hash.length << 1);
        for (int i = 0; i < hash.length; i++) {
            String digit = Integer.toHexString(0xFF & hash[i]);
            if (digit.length() == 1) {
                digit = '0' + digit;
            }
            sb.append(digit);
        }
        return sb.toString();
    }
}
