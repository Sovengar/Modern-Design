package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design._shared.vo.AccountMoney;
import jonathan.modern_design.auth.api.UserApi;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.api.events.AccountCreated;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static jonathan.modern_design._shared.TraceIdGenerator.generateTraceId;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class CreateAccountHttpController {
    private final CreateAccount createAccount;
    private final AccountRepo repository;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Create Account")
    @Transactional
    public ResponseEntity<Response<AccountDto>> createAccount(@RequestBody @Valid final CreateAccount.Command message) {
        generateTraceId();
        //Authentication + Authorization

        log.info("START createAccount with command: {}", message);
        final var accountNumber = createAccount.handle(message).getAccountNumber();

        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountNumber}").buildAndExpand(accountNumber).toUri();

        log.info("END Account created  with number: {}", accountNumber);
        return ResponseEntity.created(location).body(
                new Response.Builder<AccountDto>()
                        .data(new AccountDto(account))
                        .links(List.of(new Response.Link("findAccount", "/accounts/" + accountNumber, "GET")))
                        //Actions? This would have to be updated to support the new API, smells...
                        .withDefaultMetadataV1()
        );
    }
}

@RequiredArgsConstructor
@Slf4j
@ApplicationService
public class CreateAccount {
    private final AccountRepo repository;
    private final UserApi userFacade;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public AccountNumber handle(final Command message) {
        log.info("START - Creating account with command: {}", message);

        final var currency = Currency.fromCode(message.currency());
        final var account = Account.Factory.create(AccountNumber.of(AccountNumberGenerator.generate()), AccountMoney.of(BigDecimal.ZERO, currency));
        publisher.publishEvent(new AccountCreated(account.getAccountNumber().getAccountNumber()));

        var accountNumber = repository.create(account);
        log.info("END - Account created  with number: {}", accountNumber);
        return accountNumber;
    }

    @DomainService
    private static class AccountNumberGenerator {
        //Complex logic here... If it grows too big, move to a domainService
        public static String generate() {
            return UUID.randomUUID().toString();
        }
    }

    public record Command(
            String realname,
            @NotEmpty(message = "Email is required") String email,
            @NotEmpty(message = "Username is required") String username,
            @NotEmpty(message = "Address is required") String address,
            @NotEmpty(message = "Password is required") String password,
            @NotEmpty(message = "Country is required") String country,
            @NotNull(message = "Currency is required") String currency) {
    }
}


