package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.DomainService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.user.api.UserApi;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.domain.models.User;
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
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class CreateAccountController {
    private final CreateAccount createAccount;
    private final AccountRepo repository;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    //OPENAPI @Operation(description = "Create Account")
    @Transactional
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid final CreateAccount.Command message) {
        log.info("START Controller - Creating account with command: {}", message);
        final var accountNumber = createAccount.handle(message).accountNumber();

        var account = repository.findOneOrElseThrow(accountNumber);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountNumber}").buildAndExpand(accountNumber).toUri();

        log.info("END Controller - Account created  with number: {}", accountNumber);
        return ResponseEntity.created(location).body(new AccountDto(account));
    }
}

@RequiredArgsConstructor
@Slf4j
@ApplicationService
public class CreateAccount {
    private final AccountRepo repository;
    private final UserApi userFacade;
    private final CountriesInventory countriesInventory;

    @Transactional
    public AccountAccountNumber handle(final Command message) {
        log.info("START - Creating account with command: {}", message);

        var userId = registerUser(message);
        final var currency = Currency.fromCode(message.currency());
        final var account = Account.Factory.create(AccountAccountNumber.of(AccountNumberGenerator.generate()), AccountMoney.of(BigDecimal.ZERO, currency), AccountAddress.of(message.address()), userId);

        var accountNumber = repository.create(account);
        log.info("END - Account created  with number: {}", accountNumber);
        return accountNumber;
    }

    private User.Id registerUser(final Command message) {
        var userId = UUID.randomUUID();
        var userCreateCommand = new RegisterUser.Command(
                userId,
                ofNullable(message.realname()),
                message.username(),
                message.email(),
                message.password(),
                countriesInventory.findByCodeOrElseThrow(message.country()),
                List.of("+34123456789")); //TODO

        userFacade.registerUser(userCreateCommand);
        return new User.Id(userId);
    }

    @DomainService
    private static class AccountNumberGenerator {
        //Complex logic here... If it grows too big move to a domainService
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
            @NotEmpty(message = "Currency is required") String currency) {
    }
}


