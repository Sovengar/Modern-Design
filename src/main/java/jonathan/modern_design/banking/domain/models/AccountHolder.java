package jonathan.modern_design.banking.domain.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jonathan.modern_design._shared.domain.vo.BirthDate;
import jonathan.modern_design._shared.infra.BaseAggregateRoot;
import jonathan.modern_design._shared.tags.persistence.FKWithoutDBLink;
import jonathan.modern_design.banking.api.events.AccountHolderRegistered;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.domain.vo.AccountHolderName;
import jonathan.modern_design.banking.domain.vo.AccountHolderPhoneNumbers;
import jonathan.modern_design.banking.domain.vo.PersonalId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "account_holders", schema = "banking")
@Getter
@NoArgsConstructor(access = PRIVATE) //For Hibernate
public class AccountHolder extends BaseAggregateRoot<AccountHolder> {
    public static final String DB_PATH = "banking.account_holders";

    @Id
    @Column(name = "account_holder_id", nullable = false, updatable = false)
    private UUID id;

    @Embedded
    @Getter(PRIVATE)
    private AccountHolderName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "personal_id_value", nullable = false, length = 20)),
            @AttributeOverride(name = "type", column = @Column(name = "personal_id_type", nullable = false, length = 10)),
    })
    private PersonalId personalId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private AccountHolderAddress address;

    @Embedded
    private BirthDate birthdate;

    @Embedded
    private AccountHolderPhoneNumbers phoneNumbers;

    @FKWithoutDBLink
    private UUID userId;

    @Column(nullable = false)
    private boolean deleted = false;

    private AccountHolder(UUID id, AccountHolderName name, PersonalId personalId, AccountHolderAddress address, BirthDate birthdate, AccountHolderPhoneNumbers phoneNumbers, UUID userId) {
        this.id = Objects.nonNull(id) ? id : UUID.randomUUID();
        this.name = name;
        this.personalId = personalId;
        this.address = address;
        this.birthdate = birthdate;
        this.phoneNumbers = phoneNumbers;
        this.userId = userId;
        this.deleted = false;

        this.registerEvent(new AccountHolderRegistered(id));
    }

    public static AccountHolder create(final UUID accountHolderId, Optional<String> name, String personalId, AccountHolderAddress address, LocalDate birthDate, List<String> phoneNumbers, UUID userId) {
        var accountHolderName = AccountHolderName.of(name);
        var personalIdVO = PersonalId.of(personalId);
        var birthDateVO = BirthDate.of(birthDate);
        var phoneNumbersVO = AccountHolderPhoneNumbers.of(phoneNumbers);

        return new AccountHolder(
                accountHolderId,
                accountHolderName,
                personalIdVO,
                address,
                birthDateVO,
                phoneNumbersVO,
                userId
        );
    }

    public String getNameOrPlaceHolder() {
        return name.getOptionalName().orElse("Not defined");
    }
}
