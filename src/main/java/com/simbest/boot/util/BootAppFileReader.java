/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 用途：读取Boot项目Classpath路径下的文件
 * 作者: lishuyi
 * 时间: 2018/8/2  21:13
 */
@Slf4j
public class BootAppFileReader {

    public static BufferedReader getClasspathFile(String filepath){
        filepath = ResourceUtils.CLASSPATH_URL_PREFIX + filepath;
        BufferedReader bufferedReader = null;
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            InputStream inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e){
            try {
                bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile(filepath)));
            } catch (FileNotFoundException e1) {
            }
        }
        if(bufferedReader == null){
            log.error("######################Load classpath file {} error.", filepath);
        }
        return bufferedReader;
    }
}
