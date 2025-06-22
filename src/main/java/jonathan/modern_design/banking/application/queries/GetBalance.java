package jonathan.modern_design.banking.application.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.domain.tags.DataAdapter;
import jonathan.modern_design._shared.domain.tags.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.banking.domain.models.QAccountEntity.accountEntity;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class GetBalanceHttpController {
    private final GetBalance querier;

    @Operation(description = "Get Balance of the account")
    @GetMapping(path = "/{accountNumber}/balance")
    public ResponseEntity<Response<BigDecimal>> getBalance(@PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN getBalance for accountNumber: {}", accountNumber);
        var balance = querier.getBalance(accountNumber);
        log.info("END getBalance for accountNumber: {}", accountNumber);

        return ResponseEntity.ok(new Response.Builder<BigDecimal>().data(balance).withDefaultMetadataV1());
    }
}

@DataAdapter
@RequiredArgsConstructor
class GetBalance {
    @PersistenceContext
    private final EntityManager entityManager;

    public BigDecimal getBalance(final String accountNumber) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        return queryFactory
                .select(accountEntity.balance)
                .from(accountEntity)
                .where(accountEntity.accountNumber.eq(accountNumber))
                .fetchOne();
    }
}
