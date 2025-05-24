package jonathan.modern_design.account_module.application.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.tags.DataAdapter;
import jonathan.modern_design._common.tags.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.account_module.domain.models.account.QAccountEntity.accountEntity;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class GetBalanceHttpController {
    private final GetBalance querier;
    private final ObservationRegistry registry;

    @Operation(description = "Get Balance of the account")
    @GetMapping(path = "/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();

        //TODO SECURITY, YOU CAN'T ACCESS ANY ACCOUNT IF YOU ARE NOT AN ADMIN

        //Fine-grained Observation instead of @Observation
        String prefix = accountNumber.split("-")[0];
        var observation = Observation
                .createNotStarted("get-balance", registry)
                .lowCardinalityKeyValue("accountPrefix", prefix)
                .contextualName("getBalance");

        log.info("BEGIN getBalance for accountNumber: {}", accountNumber);
        var balance = observation.observe(() -> querier.getBalance(accountNumber));
        log.info("END getBalance for accountNumber: {}", accountNumber);

        return ResponseEntity.ok().body(balance);
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

/*
@Slf4j
class LoggingObservationHandler implements ObservationHandler<Observation.Context> {

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true; // Maneja todos los contextos
    }

    @Override
    public void onStart(Observation.Context context) {
        log.info("Observation started: {}", context.getName());
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("Observation stopped: {}", context.getName());
    }

    @Override
    public void onError(Observation.Context context) {
        log.error("Observation error: {}", context.getName(), context.getError());
    }
}
*/
