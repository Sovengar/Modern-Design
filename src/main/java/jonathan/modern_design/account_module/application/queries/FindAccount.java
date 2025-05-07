package jonathan.modern_design.account_module.application.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.tags.DataAdapter;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.account_module.domain.models.account.QAccountEntity.accountEntity;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class FindAccountHttpController {
    private final FindAccount findAccount;

    @Operation(description = "Find Account")
    @Observed(name = "findAccount")
    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountDto> loadAccount(@PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();

        log.info("BEGIN FindAccount for accountNumber: {}", accountNumber);
        var account = findAccount.queryWith(accountNumber);
        log.info("END FindAccount for accountNumber: {}", accountNumber);

        return ResponseEntity.ok().body(account);
    }
}

@DataAdapter
@RequiredArgsConstructor
public class FindAccount {
    @PersistenceContext
    private final EntityManager entityManager;

    public AccountDto queryWith(final String accountNumber) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        var account = queryFactory.selectFrom(accountEntity)
                .where(accountEntity.accountNumber.eq(accountNumber))
                .fetchOne();

        assert account != null;
        return new AccountDto(account);
    }
}
