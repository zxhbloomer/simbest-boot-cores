package com.simbest.boot.sys.service.impl;

import com.google.common.collect.Lists;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.sys.model.UploadFileResponse;
import com.simbest.boot.sys.repository.SysFileRepository;
import com.simbest.boot.sys.service.ISysFileService;
import com.simbest.boot.util.AppFileUtil;
import com.simbest.boot.util.office.ExcelUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SysFileService extends LogicService<SysFile, Long> implements ISysFileService {

    @Autowired
    private SysFileRepository repository;

    @Autowired
    private AppFileUtil appFileUtil;

    @Autowired
    public SysFileService(SysFileRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public SysFile uploadProcessFile(MultipartFile multipartFile, String pmInsType, Long pmInsId, String pmInsTypePart) {
        List<SysFile> fileList = uploadProcessFiles(new MultipartFile[]{multipartFile}, pmInsType, pmInsId, pmInsTypePart);
        return fileList.isEmpty() ? null : fileList.get(0);
    }

    @Override
    @Transactional
    public List<SysFile> uploadProcessFiles(MultipartFile[] multipartFiles, String pmInsType, Long pmInsId,String pmInsTypePart) {
        List<SysFile> sysFileList = Lists.newArrayList();
        try {
            sysFileList = appFileUtil.uploadFiles(pmInsType + ApplicationConstants.SLASH + pmInsTypePart, multipartFiles);
            for(SysFile sysFile : sysFileList){
                sysFile = super.insert(sysFile); //先保存文件获取ID
                sysFile.setDownLoadUrl(sysFile.getDownLoadUrl().concat("?id="+sysFile.getId())); //修改下载URL，追加ID
                sysFile.setPmInsType(pmInsType);
                sysFile.setPmInsId(pmInsId);
                sysFile.setPmInsTypePart(pmInsTypePart);
            }
        } catch (IOException e) {
            Exceptions.printException(e);
        }
        return sysFileList;
    }

    @Override
    @Transactional
    public <T> UploadFileResponse importExcel(MultipartFile multipartFile, String pmInsType, Long pmInsId, String pmInsTypePart, Class<T> clazz, String sheetName) {
        SysFile sysFile = uploadProcessFile(multipartFile, pmInsType, pmInsId, pmInsTypePart);
        if (sysFile != null) {
            ExcelUtil<T> importUtil = new ExcelUtil<>(clazz);
            File tempFile = AppFileUtil.createTempFile();
            try {
                multipartFile.transferTo(tempFile);
                List<T> listData = importUtil.importExcel(sheetName, new FileInputStream(tempFile));
                UploadFileResponse<T> uploadFileResponse = new UploadFileResponse<>();
                uploadFileResponse.setListData(listData);
                uploadFileResponse.setSysFiles(Arrays.asList(sysFile));
                return uploadFileResponse;
            } catch (IOException e) {
                Exceptions.printException(e);
            }
        }
        return null;
    }
}
