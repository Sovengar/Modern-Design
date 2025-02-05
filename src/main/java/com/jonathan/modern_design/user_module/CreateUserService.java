package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.common.UseCase;
import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
class CreateUserService implements CreateUserUseCase {
    private final UserRepository repository;

    @Override
    public User createUser(CreateUserCommand command) {
        final var user = User.builder()
            .name(command.name())
                .firstname(command.firstname())
                .lastname(command.lastname())
                .email(command.email())
                .password(command.password())
                .country(command.country())
                .build();

        return repository.createUser(user);
    }
}
