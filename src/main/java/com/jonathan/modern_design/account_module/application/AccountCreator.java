package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._internal.config.annotations.Inyectable;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import com.jonathan.modern_design.user_module.UserApi;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import com.jonathan.modern_design.user_module.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
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
                countriesInventory.findByCodeOrElseThrow(command.country()));

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
