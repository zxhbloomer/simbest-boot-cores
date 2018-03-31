package com.simbest.boot.sys.service;

import com.simbest.boot.sys.model.SysFile;

import java.util.List;

public interface ISysFileService {

    /**
     * 保存文件信息至数据库
     * @param sysFiles sysFiles
     * @return List<SysFile>
     */
    List<SysFile> saveAll(Iterable<SysFile> sysFiles);

    /**
     * 读取文件信息
     * @param id
     * @return
     */
    SysFile findById(Long id);
}
