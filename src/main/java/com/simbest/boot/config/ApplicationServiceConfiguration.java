/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.simbest.boot.base.service.ISystemService;
import com.simbest.boot.base.service.impl.SystemService;
import com.simbest.boot.sys.repository.SysTaskExecutedLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用途：应用层服务Service泛型配置
 * 作者: lishuyi
 * 时间: 2018/6/10  10:18
 */
@Configuration
public class ApplicationServiceConfiguration {

    @Bean(name = "sysTaskExecutedLogService")
    @Autowired
    public ISystemService sysTaskExecutedLogService(SysTaskExecutedLogRepository repository) {
        return new SystemService(repository);
    }
}
