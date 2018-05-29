package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.BaseRepository;
import com.simbest.boot.sys.model.SysTaskExecutedLog;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExecutedLogRepository extends BaseRepository<SysTaskExecutedLog, Long> {


}
