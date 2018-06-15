/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 用途：默认数据源配置
 * 作者: lishuyi
 * 时间: 2018/6/13  0:42
 */
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef="entityManagerFactoryDefault",
//        transactionManagerRef="transactionManagerDefault",
//        basePackages= { "com.simbest.boot.base.repository" }) //设置Repository所在位置
public class DefaultDataSourceConfigJpa {
    @Autowired
    @Qualifier("defaultDataSource")
    private DataSource defaultDataSource;

    @Primary
    @Bean(name = "entityManagerDefault")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryDefault(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryDefault")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryDefault (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(defaultDataSource)
                .properties(getVendorProperties(defaultDataSource))
                .packages("com.didispace.domain.p") //设置实体类所在位置
                .persistenceUnit("defaultPersistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        //return jpaProperties.getHibernateProperties(dataSource);
        return null;
    }

    @Primary
    @Bean(name = "transactionManagerDefault")
    public PlatformTransactionManager transactionManagerDefault(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryDefault(builder).getObject());
    }

}
