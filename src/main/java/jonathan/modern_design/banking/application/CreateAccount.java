package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.domain.Currency;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.auth.api.UserApi;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.domain.policies.AccountNumberGenerator;
import jonathan.modern_design.banking.domain.store.AccountHolderRepo;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;


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
    private final AccountHolderRepo accountHolderRepo;
    private final UserApi userApi;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public AccountNumber handle(final Command cmd) {
        log.info("START - Creating account with command: {}", cmd);

        ComplexDomainService.handle();

        final var currency = Currency.fromCode(cmd.currency());
        final var account = Account.Factory.create(AccountNumber.of(accountNumberGenerator.generate()), Money.of(BigDecimal.ZERO, currency));
        var accountNumber = repository.create(account);

        //If you want temporal decoupling, send the command to a queue.
        var userId = registerUser(cmd);

        var accountHolder = AccountHolder.create(cmd.realname(), cmd.personalId(), cmd.country(), cmd.address(), cmd.birthdate(), cmd.phoneNumbers(), userId.getUserId());
        accountHolderRepo.save(accountHolder);

        log.info("END - Account created  with number: {}", accountNumber);
        return accountNumber;
    }

    private User.Id registerUser(final Command cmd) {
        var userId = UUID.randomUUID();
        var userCreateCommand = new RegisterUser.Command(
                userId,
                ofNullable(cmd.realname()),
                cmd.username(),
                cmd.email(),
                cmd.password(),
                cmd.country(),
                cmd.phoneNumbers());

        userApi.registerUser(userCreateCommand);
        return User.Id.of(userId);
    }

    @DomainService
    @Slf4j
    private static class ComplexDomainService {
        // Imagine complex logic here... If it grows too large or has to be consumed by other use cases, move to the domain / service folder

        public static void handle() {
            log.info("Doing complex logic...");
            log.info("Ended doing complex logic...");
        }
    }

    public record Command(
            String realname,
            @NotEmpty(message = "Email is required") String email,
            @NotEmpty(message = "Username is required") String username,
            @NotNull(message = "Address is required") AccountHolderAddress address,
            @NotEmpty(message = "Password is required") String password,
            @NotEmpty(message = "Country is required") String country,
            @NotNull(message = "Currency is required") String currency,
            @NotNull(message = "PhoneNumbers is required") List<String> phoneNumbers,
            @NotNull(message = "Birthdate is required") LocalDate birthdate,
            @NotEmpty(message = "Personal Id is required") String personalId
    ) {
    }
}


