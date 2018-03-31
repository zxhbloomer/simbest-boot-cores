package com.simbest.boot.sys.service.impl;

import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.sys.repository.SysFileRepository;
import com.simbest.boot.sys.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysFileService implements ISysFileService {

    @Autowired
    private SysFileRepository fileRepository;

    @Transactional
    public List<SysFile> saveAll(Iterable<SysFile> sysFiles) {
        return fileRepository.saveAll(sysFiles);
    }

    @Override
    public SysFile findById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }


}
