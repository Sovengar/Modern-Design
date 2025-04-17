package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.dtos.CreateAccountCommand;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.User.UserId;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
@Validated
class AccountCreatorController {
    private final AccountCreator accountCreator;
    private final AccountRepo repository;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    //OPENAPI @Operation(description = "Create Account")
    @Transactional
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid final CreateAccountCommand message) {
        log.info("START Controller - Creating account with command: {}", message);
        final var accountNumber = accountCreator.createAccount(message).accountNumber();

        var account = repository.findOneOrElseThrow(accountNumber);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountNumber}").buildAndExpand(accountNumber).toUri();

        log.info("END Controller - Account created  with number: {}", accountNumber);
        return ResponseEntity.created(location).body(new AccountDto(account));
    }
}

@Injectable
@RequiredArgsConstructor
@Slf4j
public class AccountCreator {
    private final AccountRepo repository;
    private final UserApi userFacade;
    private final CountriesInventory countriesInventory;

    public AccountAccountNumber createAccount(final CreateAccountCommand message) {
        log.info("START - Creating account with command: {}", message);

        var userId = registerUser(message);
        final var currency = Currency.fromCode(message.currency());
        final var account = Account.create(AccountAccountNumber.of(AccountNumberGenerator.generate()), AccountMoney.of(BigDecimal.ZERO, currency), AccountAddress.of(message.address()), userId);

        var accountNumber = repository.create(account);
        log.info("END - Account created  with number: {}", accountNumber);
        return accountNumber;
    }

    private User.UserId registerUser(final CreateAccountCommand command) {
        var userId = UUID.randomUUID();
        var userCreateCommand = new UserRegisterCommand(
                userId,
                ofNullable(command.realname()),
                command.username(),
                command.email(),
                command.password(),
                countriesInventory.findByCodeOrElseThrow(command.country()),
                List.of("+34123456789")); //TODO

        userFacade.registerUser(userCreateCommand);
        return new UserId(userId);
    }

    private static class AccountNumberGenerator {
        //Complex logic here... If it grows too big move to a domainService
        public static String generate() {
            return UUID.randomUUID().toString();
        }
    }
}

