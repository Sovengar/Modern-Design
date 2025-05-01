package jonathan.modern_design.user.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design.user.domain.catalogs.Roles;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "roles", schema = "md")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//If Role gets usecases, like managing permissions, creating and deleting roles dynamically based on business rules
//Then it will be an Aggregate Root
public class Role {
    @EmbeddedId
    private Role.Code code;
    private String description;

    public static Role of(Roles roleEnum) {
        return new Role(Role.Code.of(roleEnum.code()), roleEnum.description());
    }

    //IMPORTANT: Avoid indirect link with user

    @Embeddable
    @Value //Not a record for Hibernate
    @NoArgsConstructor(force = true) //For Hibernate
    @RequiredArgsConstructor(staticName = "of")
    public static class Code implements Serializable {
        @Serial private static final long serialVersionUID = -491353586550215623L;
        @Column(name = "role_code", updatable = false)
        @NotNull
        String roleCode;
    }
}
