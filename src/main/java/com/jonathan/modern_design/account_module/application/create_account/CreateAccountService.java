package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.shared.Currency;
import com.jonathan.modern_design.shared.annotations.DomainService;
import com.jonathan.modern_design.user_module.User;
import com.jonathan.modern_design.user_module.UserFacade;
import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@DomainService
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepository repository;
    private final UserFacade userFacade;

    @Transactional
    @Override
    public Account createAccount(@NonNull final AccountDataCommand command) {
        var user = createUser(command);
        Currency currency = Currency.fromCode(command.currency());
        final var account = Account.create(BigDecimal.valueOf(0), currency, command.address(), user);
        return repository.create(account);
    }

    private User createUser(AccountDataCommand command) {
        var userCreateCommand = CreateUserCommand.builder()
                .realname(command.realname())
                .username(command.username())
                .email(command.email())
                .password(command.password())
                .country(command.country())
                .build();

        return userFacade.createUser(userCreateCommand);
    }
}
