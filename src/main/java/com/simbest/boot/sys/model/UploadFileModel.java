/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用途：上传系统文件实体
 * 作者: lishuyi
 * 时间: 2018/3/7  23:10
 */
@Data
@Builder
public class UploadFileModel implements Serializable {

    private String fileName;

    private String fileType;

    private String filePath;

    private long fileSize;

    private MultipartFile[] files;

    @Override
    public String toString() {
        return "UploadModel{" +"files=" + Arrays.toString(files) + '}';
    }
}
