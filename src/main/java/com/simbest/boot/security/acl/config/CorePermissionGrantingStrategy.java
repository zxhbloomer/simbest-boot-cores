/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.acl.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;

import java.util.List;
import java.util.Objects;

/**
 * 用途：ACL访问控制鉴权
 * 作者: lishuyi
 * 时间: 2018/7/24  9:28
 */
@Slf4j
public class CorePermissionGrantingStrategy implements PermissionGrantingStrategy {

    private final transient AuditLogger auditLogger;

    public CorePermissionGrantingStrategy(AuditLogger auditLogger) {
        this.auditLogger = Objects.requireNonNull(auditLogger, "AuditLogger cannot be null");
    }

    @Override
    public boolean isGranted(Acl acl, List<Permission> permission, List<Sid> sids, boolean administrativeMode) {
        List<AccessControlEntry> aces = acl.getEntries();
        AccessControlEntry firstRejection = null;
        for (Permission p : permission) {
            for (Sid sid : sids) {
                boolean scanNextSid = true;
                for (AccessControlEntry ace : aces) {
                    if (ace.getPermission().getMask() >= p.getMask() && ace.getSid().equals(sid)) {
                        if (ace.isGranting()) {
                            if (!administrativeMode) {
                                this.auditLogger.logIfNeeded(true, ace);
                            }
                            return true;
                        }
                        if (firstRejection == null) {
                            firstRejection = ace;
                        }
                        scanNextSid = false;
                        break;
                    }
                }
                if (!scanNextSid) {
                    break;
                }
            }
        }

        if (firstRejection != null) {
            if (!administrativeMode) {
                this.auditLogger.logIfNeeded(false, firstRejection);
            }

            return false;
        } else if (acl.isEntriesInheriting() && acl.getParentAcl() != null) {
            return acl.getParentAcl().isGranted(permission, sids, false);
        } else {
            throw new NotFoundException("Unable to locate a matching ACE for passed permissions and SIDs");
        }
    }
}