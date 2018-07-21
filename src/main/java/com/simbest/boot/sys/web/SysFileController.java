/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.simbest.boot.base.web.controller.LogicController;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.sys.model.UploadFileResponse;
import com.simbest.boot.sys.service.ISysFileService;
import com.simbest.boot.util.AppFileUtil;
import com.simbest.boot.util.office.ExcelUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用途：统一系统文件管理控制器
 * 作者: lishuyi https://www.mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
 * 时间: 2018/2/23  10:14
 */
@Slf4j
@RestController
public class SysFileController extends LogicController<SysFile, Long> {

    public final static String UPLOAD_PROCESS_FILE_URL = "/sys/file/uploadProcessFile";

    public final static String UPLOAD_PROCESS_FILES_URL = "/sys/file/uploadProcessFiles";

    public final static String DOWNLOAD_URL = "/sys/file/download";

    @Autowired
    private ISysFileService fileService;

    @Autowired
    public SysFileController(ISysFileService fileService) {
        super(fileService);
        this.fileService = fileService;
    }

    /**
     *
     * @param uploadfile
     * @return
     */
    @ApiOperation(value = "上传单个流程附件", notes = "会保存到数据库SYS_FILE")
    @PostMapping(UPLOAD_PROCESS_FILE_URL)
    public JsonResponse uploadProcessFile(@RequestParam("file") MultipartFile uploadfile,
                                          @RequestParam("pmInsType") String pmInsType,
                                          @RequestParam(value = "pmInsId", required = false) Long pmInsId, //起草阶段上传文件，可不填写业务单据ID
                                          @RequestParam("pmInsTypePart") String pmInsTypePart) {
        return this.uploadProcessFiles(new MultipartFile[]{uploadfile}, pmInsType, pmInsId, pmInsTypePart);
    }

    /**
     *
     * @param uploadfiles
     * @return
     */
    @ApiOperation(value = "上传多个流程附件", notes = "会保存到数据库SYS_FILE")
    @PostMapping(UPLOAD_PROCESS_FILES_URL)
    public JsonResponse uploadProcessFiles(@RequestParam("files") MultipartFile[] uploadfiles,
                                           @RequestParam("pmInsType") String pmInsType,
                                           @RequestParam(value = "pmInsId", required = false) Long pmInsId, //起草阶段上传文件，可不填写业务单据ID
                                           @RequestParam("pmInsTypePart") String pmInsTypePart) {
        for(MultipartFile uploadFile : uploadfiles){
            if(!AppFileUtil.validateUploadFileType(uploadFile.getOriginalFilename())){
                return JsonResponse.fail("不允许上传的文件类型");
            }
        }
        List<SysFile> sysFiles = fileService.uploadProcessFiles(uploadfiles, pmInsType, pmInsId, pmInsTypePart);
        UploadFileResponse uploadFileResponse = new UploadFileResponse();
        uploadFileResponse.setSysFiles(sysFiles);
        return JsonResponse.success(uploadFileResponse);
    }

    @GetMapping(DOWNLOAD_URL)
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

    /**
     * 不直接暴露接口
     * @param uploadfile
     * @param pmInsType
     * @param pmInsId
     * @param pmInsTypePart
     * @param clazz
     * @param sheetName
     * @param <T>
     * @rn
     * @throws IOException
     */
    private <T> JsonResponse importExcel(MultipartFile uploadfile,
                                        String pmInsType,
                                        Long pmInsId, //起草阶段上传文件，可不填写业务单据ID
                                        String pmInsTypePart,
                                        Class<T> clazz,  String sheetName) throws IOException {
        UploadFileResponse uploadFileResponse = fileService.importExcel(uploadfile, pmInsType, pmInsId, pmInsTypePart, clazz, sheetName);
        if(null != uploadFileResponse){
            return JsonResponse.success(uploadFileResponse);
        } else {
            return  JsonResponse.defaultErrorResponse();
        }
    }
}
