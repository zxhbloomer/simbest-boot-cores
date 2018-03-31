/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.encrypt;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * SHA-1加密工具类
 * @author lishuyi
 * 时间: 2017/12/28  22:02 
 */
@Component
public class ShaEncryptor extends AbstractEncryptor {

    private static SecureRandom random = new SecureRandom();

//    @SuppressWarnings("uncommentedmain")
//    public static void main(String[] args) {
//        ShaEncryptor encryptor = new ShaEncryptor();
//        encryptor.encrypt("1");
//        encryptor.encryptSource("1");
//    }

    /**
     * 对目标字符串进行SHA-1加密
     * @param source 目标字符串
     * @return 加密字符串
     */
    @Override
    protected String encryptSource(String source) {
        return encryptAlgorithm(source, SHA1);
    }

    /**
     *
     * @param code 编码
     * @return 空
     */
    @Override
    protected String decryptCode(String code) {
        return null;
    }
}
