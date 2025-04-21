package jonathan.modern_design.account_module.infra.store;

import jonathan.modern_design.account_module.domain.models.account.AccountEntity;
import jonathan.modern_design.user.domain.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AccountRepoSpringDataJPA extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);

    // ✨ Stream para procesamiento eficiente de grandes cantidades de datos
    @Query("SELECT a FROM AccountEntity a WHERE a.active = true")
    Stream<AccountEntity> streamAllActiveAccounts();

    @Query("SELECT a FROM AccountEntity a WHERE a.userId = :userId AND a.active = true")
    List<AccountEntity> findActiveAccountsByUserId(@Param("userId") User.UserId userId);

    List<AccountEntity> findByUserIdOrderByDateOfLastTransactionDesc(User.UserId userId);

    long countByActiveTrue();

    // ✨ Native query
    @Query(
            value = "SELECT * FROM md.accounts WHERE balance > :minBalance AND deleted <> true",
            nativeQuery = true
    )
    List<AccountEntity> findAccountsWithBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);

    // ✨ Dynamic projection
    <T> List<T> findByAccountNumber(String accountNumber, Class<T> type);
}
