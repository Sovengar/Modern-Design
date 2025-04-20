package jonathan.modern_design._common;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class UserAuditListener {

    @Value("${application.name}")
    private String appUser;

    @PrePersist
    public void setCreatedBy(AuditingColumns auditingColumns) {
        final var currentUser = getCurrentUser();
        final var userCreatingEntity = auditingColumns.createdBy();
        final var finalUser = StringUtils.hasText(userCreatingEntity) ? userCreatingEntity : currentUser;

        auditingColumns.createdOn(LocalDateTime.now());
        auditingColumns.createdBy(finalUser);
        auditingColumns.modifiedBy(finalUser);
    }

    @PreUpdate
    public void setUpdatedBy(AuditingColumns auditingColumns) {
        String currentUser = getCurrentUser();
        auditingColumns.modifiedBy(currentUser);
        auditingColumns.modifiedOn(LocalDateTime.now());
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
//            if (authentication.getPrincipal() instanceof AccessToken accessToken) {
//                return accessToken.document() + ";" + accessToken.name();
//            }
            return authentication.getName();
        } else {
            return appUser;
        }
    }
}
