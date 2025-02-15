package com.jonathan.modern_design.account_module.application.deposit;

import com.jonathan.modern_design.account_module.domain.model.Account;

public interface DepositUseCase {
    Account deposit(DepositCommand command);
}
