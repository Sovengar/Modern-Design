package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design._shared.country.CountriesInventory;
import com.jonathan.modern_design.account_module.application.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.user_module.UserFacade;
import com.jonathan.modern_design.user_module.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@DomainService
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepository repository;
    private final UserFacade userFacade;
    private final CountriesInventory countriesInventory;

    public AccountNumber createAccount(final CreateAccountCommand command) {
        var userId = registerUser(command);
        final var currency = Currency.fromCode(command.currency());

        final var account = Account.create(AccountNumberGenerator.generate(), BigDecimal.valueOf(0), currency, command.address(), userId);
        return repository.create(account);
    }

    private User.ID registerUser(final CreateAccountCommand command) {
        var userCreateCommand = new RegisterUserUseCase.RegisterUserCommand(
                UUID.randomUUID(),
                ofNullable(command.realname()),
                command.username(),
                command.email(),
                command.password(),
                countriesInventory.findByCodeOrElseThrow(command.country()));

        return userFacade.registerUser(userCreateCommand);
    }

    private static class AccountNumberGenerator {
        public static String generate() {
            return UUID.randomUUID().toString();
        }
    }
}
