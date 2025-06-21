package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design.auth.domain.models.User;
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

public interface AccountRepoSpringDataJPA extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);

    default AccountEntity findByAccNumberOrElseThrow(@NonNull final String accountNumber) {
        return findByAccountNumber(accountNumber).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    // ✨ Stream para procesamiento eficiente de grandes cantidades de datos
    @Query("SELECT a FROM AccountEntity a WHERE a.status = ACTIVE")
    Stream<AccountEntity> streamAllActiveAccounts();

    @Query("SELECT a FROM AccountEntity a WHERE a.userId = :userId AND a.status = ACTIVE")
    List<AccountEntity> findActiveAccountsByUserId(@Param("userId") User.Id userId);

    List<AccountEntity> findByUserIdOrderByBalanceDesc(User.Id userId);

    //long countByCurrency();

    // ✨ Native query
    @Query(
            value = "SELECT * FROM md.accounts WHERE balance > :minBalance",
            nativeQuery = true
    )
    List<AccountEntity> findAccountsWithBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);

    // ✨ Dynamic projection
    <T> List<T> findByAccountNumber(String accountNumber, Class<T> type);

    // Explicit projection
    AccountDto findAccountDtoByAccountNumber(String accountNumber);
}
