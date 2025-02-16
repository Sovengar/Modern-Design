package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.user_module.application.CreateUserCommand;
import com.jonathan.modern_design.user_module.application.UserFacade;
import com.jonathan.modern_design.user_module.domain.User;
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
        var user = createUser(command);
        final var currency = Currency.fromCode(command.currency());
        final var account = Account.create(BigDecimal.valueOf(0), currency, command.address(), user);
        return repository.create(account);
    }

    private User createUser(CreateAccountCommand command) {
        var userCreateCommand = CreateUserCommand.builder()
                .uuid(UUID.randomUUID())
                .realname(command.realname())
                .username(command.username())
                .email(command.email())
                .password(command.password())
                .country(command.country())
                .build();

        return userFacade.createUser(userCreateCommand);
    }
}
