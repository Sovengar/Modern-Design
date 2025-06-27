package jonathan.modern_design.banking.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.banking.domain.models.QAccountEntity.accountEntity;
import static jonathan.modern_design.banking.domain.models.QAccountHolder.accountHolder;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
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

        return ResponseEntity.ok(new Response.Builder<AccountDto>().data(account).withDefaultMetadataV1());
        //Actions? This would have to be updated to support the new API, smells...
    }
}

@DataAdapter
public class FindAccount {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    FindAccount(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    public AccountDto queryWith(final String accountNumber) {
        var account = queryFactory.selectFrom(accountEntity)
                .where(accountEntity.accountNumber.eq(accountNumber))
                .fetchOne();

        assert account != null;
        return new AccountDto(account);
    }

    public AccountDto queryWithUserId(final UUID userId) {
        var account = queryFactory.selectFrom(accountEntity)
                .join(accountEntity.accountHolder)
                .where(accountHolder.userId.eq(userId))
                .fetchOne();

        assert account != null;
        return new AccountDto(account);
    }
}
