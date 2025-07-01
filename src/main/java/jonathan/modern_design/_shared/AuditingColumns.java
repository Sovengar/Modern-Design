package jonathan.modern_design._shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingColumns {
    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @CreatedDate
    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedBy
    @Column(insertable = false)
    protected String modifiedBy;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(insertable = false)
    protected LocalDateTime modifiedAt;
}

