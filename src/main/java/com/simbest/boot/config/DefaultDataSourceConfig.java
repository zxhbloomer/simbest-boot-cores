/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 用途：默认数据源配置
 * 作者: lishuyi
 * 时间: 2018/6/13  0:42
 */
//@Configuration
public class DefaultDataSourceConfig {

    @Bean(name = "defaultDataSource")
    @Qualifier("defaultDataSource")
    @ConfigurationProperties(prefix="spring.datasource.default")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
