package jonathan.modern_design.search.store;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static lombok.AccessLevel.PACKAGE;

@Entity
@Table(name = "account_with_user_info", schema = "search")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor
@Builder //Allowed because is a class without biz logic, use only for mapping or testing purposes
public class AccountWithUserInfoReadModel {
    @Id
    private UUID userId;
    private String accountNumber;
    private BigDecimal balance;
    private String username;
    private String email;
}
