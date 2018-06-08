/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.google.common.collect.Lists;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.sys.model.UploadFileModel;
import com.simbest.boot.sys.service.ISysFileService;
import com.simbest.boot.util.AppFileUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用途：统一系统文件管理控制器
 * 作者: lishuyi https://www.mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
 * 时间: 2018/2/23  10:14
 */
@Slf4j
@RestController
@RequestMapping("/sys/file")
public class SysFileController {

    @Autowired
    private AppFileUtil appFileUtil;

    @Autowired
    private ISysFileService fileService;

    /**
     * 上传单个文件
     *
     * @param uploadfile
     * @return
     */
    @PostMapping("/uploadSingle")
    public ResponseEntity<?> uploadSingle(@RequestParam("file") MultipartFile uploadfile,
                                          @RequestParam("application") String application, @RequestParam("moudule") String moudule) {
        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>(JacksonUtils.obj2json(JsonResponse.defaultErrorResponse()), HttpStatus.BAD_REQUEST);
        } else if (!AppFileUtil.validateUploadFileType(uploadfile.getOriginalFilename())) {
            return new ResponseEntity<>(JacksonUtils.obj2json(JsonResponse.defaultErrorResponse()), HttpStatus.BAD_REQUEST);
        }
        try {
            List<SysFile> sysFiles = uploadAndSave(new MultipartFile[]{uploadfile}, application, moudule);
            return new ResponseEntity<>(JacksonUtils.obj2json(JsonResponse.builder().errcode(JsonResponse.SUCCESS_CODE)
                    .data(sysFiles.get(0)).build()), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(JacksonUtils.obj2json(JsonResponse.defaultErrorResponse()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 上传多个文件
     *
     * @param uploadfiles
     * @return
     */
    @PostMapping("/uploadMulti")
    public ResponseEntity<?> uploadMulti(@RequestParam("files") MultipartFile[] uploadfiles,
                                         @RequestParam("application") String application, @RequestParam("moudule") String moudule) {
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<>(JacksonUtils.obj2json(JsonResponse.defaultErrorResponse()), HttpStatus.BAD_REQUEST);
        }
        try {
            List<SysFile> sysFiles = uploadAndSave(uploadfiles, application, moudule);
            return new ResponseEntity<>(JacksonUtils.obj2json(JsonResponse.builder().errcode(JsonResponse.SUCCESS_CODE)
                    .data(sysFiles).build()), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<SysFile> uploadAndSave(MultipartFile[] uploadfiles, String application, String moudule) throws IOException {
        List<SysFile> sysFiles = Lists.newArrayList();
        List<UploadFileModel> fileModels = appFileUtil.uploadFiles(application + ApplicationConstants.SLASH + moudule, Arrays.asList(uploadfiles));
        for (UploadFileModel m : fileModels) {
            SysFile sysFile = SysFile.builder().fileName(m.getFileName()).fileType(AppFileUtil.getFileSuffix(m.getFileName())).filePath(m.getFilePath())
                    .fileSize(m.getFileSize()).application(application).moudule(moudule).build();
            sysFile.setCreator(SecurityUtils.getCurrentUserName());
            sysFile.setModifier(SecurityUtils.getCurrentUserName());
            sysFiles.add(sysFile);
        }
        return fileService.saveAll(sysFiles);
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("id") Long id) throws FileNotFoundException {
        SysFile sysFile = fileService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // headers.setContentType(MediaType.parseMediaType(sysFile.getFileType()));
        // String downloadFileName = new String(sysFile.getFileName().getBytes("UTF-8"), "ISO-8859-1");

        ContentDisposition cd = ContentDisposition.builder("attachment")
                .filename(sysFile.getFileName(), StandardCharsets.UTF_8) // 防止文件名乱码，需指定文件名编码
                .size(sysFile.getFileSize())
                .build();
        headers.setContentDisposition(cd);
        Resource resource = new InputStreamResource(new FileInputStream(new File(sysFile.getFilePath())));
        return ResponseEntity.ok().headers(headers).body(resource);

    }
}
