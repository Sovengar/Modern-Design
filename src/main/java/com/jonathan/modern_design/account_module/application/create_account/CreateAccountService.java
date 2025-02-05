package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.common.UseCase;
import com.jonathan.modern_design.user_module.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepository repository;
    //private final CreateUserUseCase createUserUseCase;

    @Override
    public Account createAccount(@NonNull final AccountDataCommand command) {
        // var user = createUserUseCase.createUser(); TODO FIX
        var user = User.create("a","a","a","a","a","a");
        final var account = Account.builder()
                .money(AccountMoneyVO.of(BigDecimal.valueOf(0), Currency.EURO)) // TODO FIX CURRENCY
                .user(user)
                .build();
        return repository.create(account);
    }
}
