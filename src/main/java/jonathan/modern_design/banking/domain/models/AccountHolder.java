package jonathan.modern_design.banking.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jonathan.modern_design._shared.domain.vo.BirthDate;
import jonathan.modern_design._shared.infra.db.BaseAggregateRoot;
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
import java.util.UUID;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "account_holders", schema = "banking")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
public class AccountHolder extends BaseAggregateRoot<AccountHolder> {
    public static final String DB_PATH = "banking.account_holders";

    @Id
    @Column(name = "account_holder_id", nullable = false, updatable = false)
    private UUID id;
    @Embedded
    @Getter(PRIVATE)
    private AccountHolderName name;
    @Embedded
    private PersonalId personalId;
    private String country;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private AccountHolderAddress address;
    @Embedded
    private BirthDate birthdate;
    @Embedded
    private AccountHolderPhoneNumbers phoneNumbers;
    private UUID userId;
    @Column(nullable = false)
    private boolean deleted = false;

    private AccountHolder(UUID id, AccountHolderName name, PersonalId personalId, String country, AccountHolderAddress address, BirthDate birthdate, AccountHolderPhoneNumbers phoneNumbers, UUID userId) {
        this.id = id;
        this.name = name;
        this.personalId = personalId;
        this.country = country;
        this.address = address;
        this.birthdate = birthdate;
        this.phoneNumbers = phoneNumbers;
        this.userId = userId;
        this.deleted = false;

        this.registerEvent(new AccountHolderRegistered(id));
    }

    public static AccountHolder create(String name, String personalId, String country, AccountHolderAddress address, LocalDate birthDate, List<String> phoneNumbers, UUID userId) {
        var accountHolderName = AccountHolderName.of(name);
        var personalIdVO = PersonalId.of(personalId);
        //var addressVO = AccountHolderAddress.of(address);
        var birthDateVO = BirthDate.of(birthDate);
        var phoneNumbersVO = AccountHolderPhoneNumbers.of(phoneNumbers);

        return new AccountHolder(
                UUID.randomUUID(),
                accountHolderName,
                personalIdVO,
                country,
                address,
                birthDateVO,
                phoneNumbersVO,
                userId
        );
    }

    public String getNameOrPlaceHolder() {
        return name.getName().orElse("Not defined");
    }
}
