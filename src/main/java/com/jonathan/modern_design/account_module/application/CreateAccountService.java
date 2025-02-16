package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.services.CreateAccountUseCase;
import com.jonathan.modern_design.user_module.application.UserFacade;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.services.RegisterUserUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepository repository;
    private final UserFacade userFacade;

    @Transactional
    @Override
    public Account createAccount(@NonNull final CreateAccountCommand command) {
        var user = registerUser(command);
        final var currency = Currency.fromCode(command.currency());
        final var account = Account.create(BigDecimal.valueOf(0), currency, command.address(), user);
        return repository.create(account);
    }

    private User registerUser(CreateAccountCommand command) {
        var userCreateCommand = RegisterUserUseCase.RegisterUserCommand.builder()
                .uuid(UUID.randomUUID())
                .realname(command.realname())
                .username(command.username())
                .email(command.email())
                .password(command.password())
                .country(command.country())
                .build();

        return userFacade.registerUser(userCreateCommand);
    }
}
