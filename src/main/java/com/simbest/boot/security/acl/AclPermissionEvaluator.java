/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.acl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * 用途：自定义ACL访问控制
 * 作者: lishuyi
 * 时间: 2018/2/25  21:41
 * 参考: DenyAllPermissionEvaluator
 */
@Slf4j
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final Map<Class<?>, JpaEntityInformation<?, ?>> entityInformationMap = new HashMap<>();

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null) {
            return false;
        }
        try {
            Class<?> domainClass = targetDomainObject.getClass();
            return hasPermission(authentication, getId(targetDomainObject), domainClass, permission);
        } catch (Exception ex) {
            log.warn("Invalid target for AclPermissionEvaluator: {}", targetDomainObject);
            log.trace("Details: ", ex);
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {
        try {
            return hasPermission(authentication, targetId, Class.forName(targetType), permission);
        } catch (ClassNotFoundException ex) {
            log.warn("Invalid target type for AclPermissionEvaluator: {}", targetType);
            log.trace("Details: ", ex);
            return false;
        }
    }

    /**
     * Check permission by directly creating a JPA count query with ACL support for the given permission
     */
    public <T> boolean hasPermission(Authentication authentication, Object targetId,
                                     Class<T> domainClass, Object permission) {
        String permissionString = getPermissionString(permission);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<T> root = query.from(domainClass);
        query.select(builder.count(root));

        Predicate idPredicate = builder.equal(root.get(getEntityInformation(domainClass).getIdAttribute()), targetId);
//        Predicate aclPredicate = aclSpecification.toPredicate(root, query, builder, permissionString);
//        query.where(builder.and(idPredicate, aclPredicate));
        return em.createQuery(query).getSingleResult() != 0;
    }

    protected <T> Object getId(T object) {
        @SuppressWarnings("unchecked")
        Class<T> domainClass = (Class<T>) object.getClass();
        JpaEntityInformation<T, ?> entityInformation = getEntityInformation(domainClass);

        Object id = entityInformation.getId(object);
        return id;
    }

    protected <T> JpaEntityInformation<T, ?> getEntityInformation(Class<T> domainClass) {
        @SuppressWarnings("unchecked")
        JpaEntityInformation<T, ?> entityInformation = (JpaEntityInformation<T, ?>) entityInformationMap
                .get(domainClass);
        if (entityInformation == null) {
            entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
            entityInformationMap.put(domainClass, entityInformation);
        }
        return entityInformation;
    }

    protected String getPermissionString(Object permission) {
        return permission.toString();
    }
}
