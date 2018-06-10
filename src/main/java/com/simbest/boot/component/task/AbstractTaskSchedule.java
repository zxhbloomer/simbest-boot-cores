/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.component.task;

import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.sys.model.SysTaskExecutedLog;
import com.simbest.boot.sys.repository.SysTaskExecutedLogRepository;
import com.simbest.boot.util.redis.RedisDistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/5/29  15:51
 */
@Slf4j
public abstract class AbstractTaskSchedule {
    public final static String CHECK_SUCCESS = "PASS";

    public final static String CHECK_FAILED = "FAILED";

    private RedisDistributedLocker distriLocker;

    private SysTaskExecutedLogRepository repository;

    public AbstractTaskSchedule(RedisDistributedLocker distriLocker, SysTaskExecutedLogRepository repository){
        this.distriLocker = distriLocker;
        this.repository = repository;
    }

    public void checkAndExecute() {
        if (distriLocker.checkMasterIsMe()) {
            log.debug("This host {} run with port {} is master, begin execute task job.", distriLocker.getHostAddress(), distriLocker.getRunningPort());
            Long beginTime = System.currentTimeMillis();
            String content = CHECK_FAILED;
            try {
                content = this.execute();
            }catch (Exception e){
                log.error("Execute taskName with {} failed.", this.getClass().getSimpleName());
                Exceptions.printException(e);
            }
            Long endTime = System.currentTimeMillis();
            SysTaskExecutedLog log = SysTaskExecutedLog.builder().taskName(this.getClass().getSimpleName()).hostname(distriLocker.getHostAddress()).port(distriLocker.getRunningPort()).durationTime(endTime - beginTime).content(StringUtils.substring(content, 0, 2000)).build();
            repository.save(log);
        } else{
            log.debug("This host {} run with port {} is not master, give up execute task job.", distriLocker.getHostAddress(), distriLocker.getRunningPort());
        }
    }

    public abstract String execute();


}
