/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.annotations;

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.constants.ApplicationConstants;
import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 用途：注解工具类
 * 作者: lishuyi
 * 时间: 2018/2/28  9:33
 */
@Component
public class AnnotationUtils {

    @Getter
    private static Map<String, String> entityCnNameClassifyMap = Maps.newHashMap();

    @PostConstruct
    void loadEntityCnNames(){
        findAnnotatedEntityCnNameClasses();
    }

    /**
     * 读取被EntityCnName注解的类
     */
    private void findAnnotatedEntityCnNameClasses() {
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(EntityCnName.class));
        provider.findCandidateComponents(ApplicationConstants.PACKAGE_NAME).forEach(this::putEntityCnNameMetadata);
    }

    /**
     * 填充读取被EntityCnName注解的类集合
     * @param beanDef
     */
    private void putEntityCnNameMetadata(BeanDefinition beanDef) {
        try {
            Class<?> cl = Class.forName(beanDef.getBeanClassName());
            EntityCnName cnName = cl.getAnnotation(EntityCnName.class);
            entityCnNameClassifyMap.put(cl.getSimpleName(), cnName.name());
        } catch (Exception e) {
            Exceptions.printException(e);
        }
    }

}
