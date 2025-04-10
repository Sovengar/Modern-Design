package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.Inyectable;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.country.CountriesInventory;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.vo.AccountNumber;
import jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.User.UserId;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Inyectable
@RequiredArgsConstructor
public class AccountCreator {
    private final AccountRepo repository;
    private final UserApi userFacade;
    private final CountriesInventory countriesInventory;

    public AccountNumber createAccount(final AccountCreatorCommand command) {
        var userId = registerUser(command);
        final var currency = Currency.fromCode(command.currency());
        final var account = Account.create(AccountNumber.of(AccountNumberGenerator.generate()), AccountMoney.of(BigDecimal.ZERO, currency), AccountAddress.of(command.address()), userId);
        return repository.create(account);
    }

    private User.UserId registerUser(final AccountCreatorCommand command) {
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
