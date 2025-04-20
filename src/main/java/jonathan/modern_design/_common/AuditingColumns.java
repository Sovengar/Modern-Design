package jonathan.modern_design._common;

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
@EntityListeners(UserAuditListener.class) //AuditingEntityListener.class from springframework
public abstract class AuditingColumns {
    @Column(name = "version")
    @Version
    protected Integer version;

    //@CreatedBy
    @Column(name = "created_by")
    protected String createdBy;

    //@CreatedDate
    @CreationTimestamp
    @Column(name = "created_on")
    protected LocalDateTime createdOn;

    //@LastModifiedBy
    @Column(name = "modified_by")
    protected String modifiedBy;

    //@LastModifiedDate
    @UpdateTimestamp
    @Column(name = "modified_on")
    protected LocalDateTime modifiedOn;

    @Column(name = "deleted", nullable = false)
    protected Boolean deleted = false;
}

