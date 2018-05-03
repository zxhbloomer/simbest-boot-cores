/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util;


import com.google.common.collect.Lists;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.sys.model.UploadFileModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用途：文件工具类
 * 作者: lishuyi
 * 时间: 2018/3/12  20:45
 */
@Component
@Slf4j
public class AppFileUtil {

    private static final String UPLOAD_FILE_PATTERN =
            "(jpg|jpeg|png|gif|bmp|doc|docx|xls|xlsx|pdf|txt)$";
    private static Pattern pattern = Pattern.compile(UPLOAD_FILE_PATTERN);

    @Value("${app.file.upload.path}")
    private String path;

    @Value("${app.file.upload.location}")
    private String location;

    private StoreLocation storeLocation = null;

    /**
     * 判断是否允许上传
     *
     * @param fileName
     * @return
     */
    public static boolean validateUploadFileType(String fileName) {
        Matcher matcher = pattern.matcher(getFileSuffix(fileName));
        return matcher.matches();
    }

    /**
     * 根据路径返回文件名，如：http://aaa/bbb.jpg C:/aaa/abc.jpg 返回abc
     *
     * @param pathToName
     * @return
     */
    public static String getFileBaseName(String pathToName) {
        Assert.notNull(pathToName, "File name can not empty!");
        return FilenameUtils.getBaseName(pathToName);
    }

    /**
     * 根据路径返回文件后缀，如：http://aaa/bbb.jpg C:/aaa/abc.jpg 返回jpg
     *
     * @param fileName hello.doc
     * @return doc
     */
    public static String getFileSuffix(String pathToName) {
        Assert.notNull(pathToName, "File name can not empty!");
        return FilenameUtils.getExtension(pathToName);
    }

    /**
     * 根据路径返回文件名，如：http://aaa/bbb.jpg C:/aaa/abc.jpg 返回abc.jpg
     *
     * @param pathToName
     * @return
     */
    public static String getFileName(String pathToName) {
        Assert.notNull(pathToName, "File name can not empty!");
        return FilenameUtils.getName(pathToName);
    }

    @PostConstruct
    public void init() {
        storeLocation = Enum.valueOf(StoreLocation.class, location);
    }

    /**
     * 上传单个文件
     *
     * @param file file
     * @return UploadFileModel
     * @throws IOException
     */
    public UploadFileModel uploadFile(String prePath, MultipartFile file) throws IOException {
        Assert.notNull(file, "Upload file can not empty!");
        return uploadFiles(prePath, Arrays.asList(file)).get(0);
    }

    /**
     * 上传多个文件
     *
     * @param files files
     * @return UploadFileModel
     * @throws IOException
     */
    public List<UploadFileModel> uploadFiles(String prePath, List<MultipartFile> files) throws IOException {
        Assert.notEmpty(files, "Upload file can not empty!");
        List<UploadFileModel> fileModels = Lists.newArrayList();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String filePath = null;
            switch (storeLocation) {
                case disk:
                    byte[] bytes = file.getBytes();
                    String storePath = path + ApplicationConstants.SLASH + prePath + ApplicationConstants.SLASH
                            + DateUtil.getCurrYear() + ApplicationConstants.SLASH
                            + DateUtil.getCurrSimpleMonth() + ApplicationConstants.SLASH
                            + DateUtil.getCurrSimpleDay() + ApplicationConstants.SLASH
                            + CodeGenerator.nextSystemUUID();
                    File targetFileDirectory = new File(storePath);
                    if (!targetFileDirectory.exists()) {
                        FileUtils.forceMkdir(targetFileDirectory);
                        log.debug("Directory {} is not exist, force create direcorty....", storePath);
                    }
                    Path path = Paths.get(targetFileDirectory.getPath() + ApplicationConstants.SLASH + file.getOriginalFilename());
                    Files.write(path, bytes);
                    filePath = path.toString();
                    break;
            }

            fileModels.add(UploadFileModel.builder().filePath(filePath).fileSize(file.getSize()).
                    fileType(getFileSuffix(file.getOriginalFilename())).fileName(file.getOriginalFilename()).build());
        }
        return fileModels;
    }

    public enum StoreLocation {disk, fastdsft, baidubos}
}
