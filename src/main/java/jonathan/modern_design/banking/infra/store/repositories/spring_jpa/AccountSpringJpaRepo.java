package jonathan.modern_design.banking.infra.store.repositories.spring_jpa;

import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AccountSpringJpaRepo extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);

    default AccountEntity findByAccNumberOrElseThrow(@NonNull final String accountNumber) {
        return findByAccountNumber(accountNumber).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    // ✨ Stream para procesamiento eficiente de grandes cantidades de datos
    @Query("SELECT a FROM AccountEntity a WHERE a.status = ACTIVE")
    Stream<AccountEntity> streamAllActiveAccounts();

    //long countByCurrency();

    // ✨ Native query
    @Query(
            value = "SELECT * FROM banking.accounts WHERE balance > :minBalance",
            nativeQuery = true
    )
    List<AccountEntity> findAccountsWithBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);

    // ✨ Dynamic projection
    <T> List<T> findByAccountNumber(String accountNumber, Class<T> type);

    // Explicit projection
    AccountDto findAccountDtoByAccountNumber(String accountNumber);
}
