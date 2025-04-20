package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.repos.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.infra.AccountDto;
import jonathan.modern_design.account_module.infra.AccountRepoSpringDataJPA;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.User.UserId;
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
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid final AccountCreator.Command message) {
        log.info("START Controller - Creating account with command: {}", message);
        final var accountNumber = accountCreator.handle(message).accountNumber();

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
    private final Storer repository;
    private final UserApi userFacade;
    private final CountriesInventory countriesInventory;

    @Transactional
    public AccountAccountNumber handle(final Command message) {
        log.info("START - Creating account with command: {}", message);

        var userId = registerUser(message);
        final var currency = Currency.fromCode(message.currency());
        final var account = Account.create(AccountAccountNumber.of(AccountNumberGenerator.generate()), AccountMoney.of(BigDecimal.ZERO, currency), AccountAddress.of(message.address()), userId);

        var accountNumber = repository.create(account);
        log.info("END - Account created  with number: {}", accountNumber);
        return accountNumber;
    }

    private User.UserId registerUser(final Command message) {
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
        return new UserId(userId);
    }

    private static class AccountNumberGenerator {
        //Complex logic here... If it grows too big move to a domainService
        public static String generate() {
            return UUID.randomUUID().toString();
        }
    }

    public record Command(String realname,
                          String email,
                          String username,
                          String address,
                          String password,
                          String country,
                          String currency) {
    }

    @DataAdapter
    @RequiredArgsConstructor
    public static class Storer {
        private final AccountRepoSpringDataJPA repository;

        public AccountAccountNumber create(final Account account) {
            var accountEntity = new AccountEntity(account);
            repository.save(accountEntity);
            return account.accountAccountNumber();
        }
    }
}


