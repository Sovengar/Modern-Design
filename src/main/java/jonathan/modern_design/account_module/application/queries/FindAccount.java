package jonathan.modern_design.account_module.application.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design.account_module.domain.models.account.QAccountEntity.accountEntity;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class FindAccountHttpController {
    private final FindAccount findAccount;

    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountDto> loadAccount(@PathVariable String accountNumber) {
        return ok(findAccount.queryWith(accountNumber));
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
