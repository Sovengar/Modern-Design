package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.user_module.User;
import com.jonathan.modern_design.user_module.UserFacade;
import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepository repository;
    private final UserFacade userFacade;

    @Override
    public Account createAccount(@NonNull final AccountDataCommand command) {
        var user = createUser(command);
        //TODO CALCULATE CURRENCY BASED ON COUNTRY
        final var account = Account.create(BigDecimal.valueOf(0), Currency.EURO, user);
        return repository.create(account);
    }

    private User createUser(AccountDataCommand command) {
        var userCreateCommand = CreateUserCommand.builder()
                .realname(command.name())
                .username(command.firstname())
                .email(command.email())
                .password(command.password())
                .country(command.country())
                .build();

        return userFacade.createUser(userCreateCommand);
    }
}
