package com.jonathan.modern_design.config.database;

import com.jonathan.modern_design.user_module.UserAuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EntityListeners(UserAuditListener.class)
public abstract class BaseEntity {
    @Column(name = "version")
    @Version
    protected Integer version;

    @Column(name = "created_by")
    protected String createdBy;

    @CreationTimestamp
    @Column(name = "created_on")
    protected LocalDateTime createdOn;

    @Column(name = "modified_by")
    protected String modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_on")
    protected LocalDateTime modifiedOn;

    @Column(name = "deleted", nullable = false)
    protected Boolean deleted = false;
}
