package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.shared.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

public class UserAuditListener {

    @Value("${application.name}")
    private String appUser;

    @PrePersist
    public void setCreatedBy(BaseEntity baseEntity) {
        final var currentUser = getCurrentUser();
        final var userCreatingEntity = baseEntity.getCreatedBy();
        final var finalUser = StringUtils.hasText(userCreatingEntity) ? userCreatingEntity : currentUser;

        baseEntity.setCreatedBy(finalUser);
        baseEntity.setModifiedBy(finalUser);
    }

    @PreUpdate
    public void setUpdatedBy(BaseEntity baseEntity) {
        String currentUser = getCurrentUser();
        baseEntity.setModifiedBy(currentUser);
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


