/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util;


import com.google.common.collect.Lists;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.sys.web.SysFileController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用途：文件工具类
 * 作者: lishuyi
 * 时间: 2018/3/12  20:45
 */
@Slf4j
@Component
public class AppFileUtil {

    private static final String UPLOAD_FILE_PATTERN =
            "(jpg|jpeg|png|gif|bmp|doc|docx|xls|xlsx|pdf|txt|rar|zip|7z)$";
    private static Pattern pattern = Pattern.compile(UPLOAD_FILE_PATTERN);

    @Value("${app.file.upload.path}")
    private String uploadPath;

    @Value("${app.file.upload.location}")
    private String uploadLocation;

    public static StoreLocation serverUploadLocation = null;

    public enum StoreLocation {disk, fastdsft, baidubos}

    @PostConstruct
    public void init() {
        serverUploadLocation = Enum.valueOf(StoreLocation.class, uploadLocation);
    }

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

    /**
     * 上传单个文件
     *
     * @param file file
     * @return SysFile
     * @throws IOException
     */
    public SysFile uploadFile(String directory, MultipartFile file) throws IOException {
        Assert.notNull(file, "Upload file can not empty!");
        return uploadFiles(directory, new MultipartFile[]{file}).get(0);
    }

    /**
     * 上传多个文件
     *
     * @param files files
     * @return UploadFileModel
     * @throws IOException
     */
    public List<SysFile> uploadFiles(String directory, MultipartFile[] files) throws IOException {
        Assert.notEmpty(files, "Upload file can not empty!");
        List<SysFile> fileModels = Lists.newArrayList();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String filePath = null;
            switch (serverUploadLocation) {
                case disk:
                    byte[] bytes = file.getBytes();
                    String storePath = uploadPath + ApplicationConstants.SLASH + directory + ApplicationConstants.SLASH
                            + DateUtil.getCurrYear() + ApplicationConstants.SLASH
                            + DateUtil.getCurrSimpleMonth() + ApplicationConstants.SLASH
                            + DateUtil.getCurrSimpleDay() + ApplicationConstants.SLASH
                            + CodeGenerator.randomChar(2);
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
            SysFile sysFile = SysFile.builder().fileName(file.getOriginalFilename()).fileType(getFileSuffix(file.getOriginalFilename()))
                    .filePath(filePath).fileSize(file.getSize()).downLoadUrl(SysFileController.DOWNLOAD_URL).
                            build();
            fileModels.add(sysFile);
        }
        return fileModels;
    }

    public static File createTempFile(){
        File tempFile = null;
        try {
            tempFile = File.createTempFile(CodeGenerator.randomChar(4), CodeGenerator.randomChar(4));
        } catch (IOException e) {
            Exceptions.printException(e);
        }
        return tempFile;
    }
}
