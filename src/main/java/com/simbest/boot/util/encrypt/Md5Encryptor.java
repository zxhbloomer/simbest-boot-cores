/**
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.encrypt;


import com.simbest.boot.base.exception.Exceptions;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * MD5加密工具类
 * @author lishuyi
 * 时间: 2017/12/28  22:02 
 */
@Component
public class Md5Encryptor extends AbstractEncryptor {

    /**
     * main函数
     * @param args 参数
     */
    public static void main(String[] args) {
        Md5Encryptor encryptor = new Md5Encryptor();
        System.out.println(encryptor.encrypt("111.com"));
        System.out.println(encryptor.encryptSource("111.com"));
    }

    /**
     * 对目标字符串进行MD5加密
     * @param source 目标字符串
     * @return 加密字符串
     */
    @Override
    protected String encryptSource(String source) {
        return encryptAlgorithm(source, MD5);
    }

    @Override
    protected String decryptCode(String code) {
        throw new UnsupportedOperationException("MD5 not support decrypt");
    }

    /**
     * 返回文件MD5值
     * @param file 文件
     * @return 文件MD5值
     * @throws IOException IO异常
     */
    public static final String getFileMd5(final File file) throws IOException {
        // IOUtils.closeQuietly已过时，这里使用try-with-resource来处理资源
        try (FileInputStream fis = new FileInputStream(file)) {
            return DigestUtils.md5DigestAsHex(IOUtils.toByteArray(fis));
        }
//        FileInputStream fis = new FileInputStream(file);
//        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
//        IOUtils.closeQuietly(fis);
//        return md5;
    }

    /**
     * 返回上传文件MD5值
     * @param uploadFile 上传文件
     * @return 文件MD5值
     */
    public static final String getMultipartFileMd5(MultipartFile uploadFile) {
        String md5 = null;
        try {
            byte[] uploadBytes = uploadFile.getBytes();
            md5 = DigestUtils.md5DigestAsHex(uploadBytes);
        } catch (IOException e) {
            Exceptions.printException(e);
        }
        if (md5 == null) {
            throw new RuntimeException();
        }
        return md5;
    }


}
