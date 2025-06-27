package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.domain.CountriesCatalog;
import jonathan.modern_design._shared.domain.Currency;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.DomainService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.domain.models.User;
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
import java.util.Optional;
import java.util.UUID;

import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class CreateAccountHttpController {
    private final CreateAccount createAccount;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Create Account")
    public ResponseEntity<Response<String>> createAccount(@RequestBody @Valid final Command cmd) {
        generateTraceId();
        //Authentication + Authorization

        log.info("START createAccount with command: {}", cmd);
        var appAddressCommand = new CreateAccount.Command.Address(cmd.address().street(), cmd.address().city(), cmd.address().state(), cmd.address().zipCode(), cmd.address().countryCode());
        var appCommand = new CreateAccount.Command(Optional.ofNullable(cmd.fullName()), cmd.email(), cmd.username(), appAddressCommand, cmd.password(), cmd.currency(), cmd.phoneNumbers(), cmd.birthdate(), cmd.personalId());
        final var accountNumber = createAccount.handle(appCommand).getAccountNumber();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountNumber}").buildAndExpand(accountNumber).toUri();

        log.info("END Account created  with number: {}", accountNumber);
        return ResponseEntity.created(location).body(
                new Response.Builder<String>()
                        .data(accountNumber)
                        .links(List.of(new Response.Link("findAccount", "/accounts/" + accountNumber, "GET")))
                        //Actions? This would have to be updated to support the new API, smells...
                        .withDefaultMetadataV1()
        );
    }

    public record Command(
            String fullName,
            @NotEmpty(message = "Email is required") String email,
            @NotEmpty(message = "Username is required") String username,
            @NotNull(message = "Address is required") CreateAccount.Command.Address address,
            @NotEmpty(message = "Password is required") String password,
            @NotNull(message = "Currency is required") String currency,
            @NotNull(message = "PhoneNumbers is required") List<String> phoneNumbers,
            @NotNull(message = "Birthdate is required") LocalDate birthdate,
            @NotEmpty(message = "Personal Id is required") String personalId
    ) {
    }
}

@RequiredArgsConstructor
@Slf4j
@ApplicationService
public class CreateAccount {
    private final AccountRepo repository;
    private final AccountHolderRepo accountHolderRepo;
    private final AuthApi authApi;
    private final AccountNumberGenerator accountNumberGenerator;
    private final CountriesCatalog countriesCatalog;

    @Transactional
    public AccountNumber handle(final Command cmd) {
        log.info("START - Creating account with command: {}", cmd);

        ComplexDomainService.handle();

        //If you want temporal decoupling, send the command to a queue.
        var userId = registerUser(cmd);

        var country = countriesCatalog.findByCodeOrElseThrow(cmd.address().countryCode());
        var address = AccountHolderAddress.of(cmd.address().street, cmd.address().city, cmd.address().state, cmd.address().zipCode, country);
        var accountHolder = AccountHolder.create(UUID.randomUUID(), cmd.fullName(), cmd.personalId(), address, cmd.birthdate(), cmd.phoneNumbers(), userId.getUserId());
        accountHolderRepo.save(accountHolder);

        final var currency = Currency.fromCode(cmd.currency());
        final var account = Account.Factory.create(AccountNumber.of(accountNumberGenerator.generate()), Money.of(BigDecimal.ZERO, currency), accountHolder);
        var accountNumber = repository.create(account);

        log.info("END - Account created  with number: {}", accountNumber);
        return accountNumber;
    }

    private User.Id registerUser(final Command cmd) {
        var userId = UUID.randomUUID();
        var userCreateCommand = new RegisterUser.Command(userId, cmd.username(), cmd.email(), cmd.password());
        authApi.registerUser(userCreateCommand);
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
            Optional<String> fullName,
            @NotEmpty(message = "Email is required") String email,
            @NotEmpty(message = "Username is required") String username,
            @NotNull(message = "Address is required") Address address,
            @NotEmpty(message = "Password is required") String password,
            @NotNull(message = "Currency is required") String currency,
            @NotNull(message = "PhoneNumbers is required") List<String> phoneNumbers,
            @NotNull(message = "Birthdate is required") LocalDate birthdate,
            @NotEmpty(message = "Personal Id is required") String personalId
    ) {

        public record Address(String street, String city, String state, String zipCode, String countryCode) {
        }
    }
}


