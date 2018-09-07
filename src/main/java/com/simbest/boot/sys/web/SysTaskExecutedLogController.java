/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.simbest.boot.base.service.ISystemService;
import com.simbest.boot.base.web.controller.GenericController;
import com.simbest.boot.sys.model.SysTaskExecutedLog;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途：定时任务日志记录控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Api(description = "SysTaskExecutedLogController", tags = {"系统管理-定时任务管理"})
@RestController
@RequestMapping("/sys/task/log")
public class SysTaskExecutedLogController extends GenericController<SysTaskExecutedLog, String> {

    @Autowired
    public SysTaskExecutedLogController(@Qualifier("sysTaskExecutedLogService")ISystemService service) {
        super(service);
    }

}
