package jonathan.modern_design.banking.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.ACCOUNTS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.banking.domain.models.QAccountEntity.accountEntity;
import static jonathan.modern_design.banking.domain.models.QAccountHolder.accountHolder;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(BANKING_MODULE_URL + ACCOUNTS_RESOURCE_URL)
class FindAccountHttpController {
    private final FindAccount findAccount;

    @Operation(description = "Find Account")
    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<Response<AccountDto>> loadAccount(@PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN FindAccount for accountNumber: {}", accountNumber);
        var account = findAccount.queryWith(accountNumber);
        log.info("END FindAccount for accountNumber: {}", accountNumber);

        //Actions? This would have to be updated to support the new API, smells...
        return account.map(accountDto -> ResponseEntity.ok(new Response.Builder<AccountDto>().data(accountDto).withDefaultMetadataV1()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

@DataAdapter
public class FindAccount {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public FindAccount(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    public Optional<AccountDto> queryWith(final String accountNumber) {
        var account = queryFactory.selectFrom(accountEntity)
                .where(accountEntity.accountNumber.eq(accountNumber))
                .fetchOne();

        return ofNullable(account).map(AccountDto::new);
    }

    public Optional<AccountDto> queryWithUserId(final UUID userId) {
        var account = queryFactory.selectFrom(accountEntity)
                .join(accountEntity.accountHolder)
                .where(accountHolder.userId.eq(userId))
                .fetchOne();

        return ofNullable(account).map(AccountDto::new);
    }
}
