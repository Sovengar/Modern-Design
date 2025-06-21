package jonathan.modern_design.banking.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jonathan.modern_design._shared.vo.BirthDate;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.domain.vo.AccountHolderName;
import jonathan.modern_design.banking.domain.vo.AccountHolderPhoneNumbers;
import jonathan.modern_design.banking.domain.vo.PersonalId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "account_holders", schema = "banking")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class AccountHolder {

    @Id
    private UUID accountHolderId;
    @Embedded
    @Getter(PRIVATE)
    private AccountHolderName name;
    @Embedded
    private PersonalId personalId;
    private String country;
    //@Embedded
    //TODO CONVERTIR EN JSONB private AccountHolderAddress address;
    @Embedded
    private BirthDate birthdate;
    @Embedded
    private AccountHolderPhoneNumbers phoneNumbers;
    private UUID userId;

    @Version
    private Integer version;
    @Column(nullable = false)
    private boolean deleted = false;

    public static AccountHolder create(String name, String personalId, String country, String address, LocalDate birthDate, AccountHolderPhoneNumbers phoneNumbers, UUID userId) {
        var accountHolderName = AccountHolderName.of(name);
        var personalIdVO = PersonalId.of(personalId);
        var addressVO = AccountHolderAddress.of(address);
        var birthDateVO = BirthDate.of(birthDate);

        return new AccountHolder(
                UUID.randomUUID(),
                accountHolderName,
                personalIdVO,
                country,
                //addressVO,
                birthDateVO,
                phoneNumbers,
                userId,
                0,
                false
        );
    }

    public String getNameOrPlaceHolder() {
        return name.getName().orElse("Not defined");
    }
}
