package jonathan.modern_design._shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAggregateRoot<A extends BaseAggregateRoot<A>> extends AbstractAggregateRoot<A> {
    //In 99% of cases optimistic locking is the correct strategy for an AR.
    @Version
    protected Long version;
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

    protected BaseAggregateRoot(Long version, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        this.version = version;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
    }
}
