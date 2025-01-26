package com.jonathan.modern_design.account.infraestructure.context;

import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.send_money.SendMoneyService;
import com.jonathan.modern_design.account.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.infraestructure.persistence.InMemoryAccountRepository;


public class AccountConfigurationFactory {

    public static AccountFacade createDefault() {
        var inMemoryDatabase = new InMemoryAccountRepository();

        FindAccountUseCase findAccountUseCase = new InMemoryAccountRepository();
        UpdateAccountUseCase updateAccountUseCase = new InMemoryAccountRepository();

        SendMoneyUseCase sendMoneyUseCase = new SendMoneyService(findAccountUseCase, updateAccountUseCase);

        return new AccountFacade(sendMoneyUseCase, findAccountUseCase, updateAccountUseCase);
    }

    public AccountFacade accountFacade() {
        var database = new InMemoryAccountRepository();

        FindAccountUseCase findAccountUseCase = database;
        UpdateAccountUseCase updateAccountUseCase = database;

        SendMoneyUseCase sendMoneyUseCase = new SendMoneyService(findAccountUseCase, updateAccountUseCase);

        return new AccountFacade(sendMoneyUseCase, findAccountUseCase, updateAccountUseCase);
    }

    public static AccountFacade createWithMocks(
            SendMoneyUseCase sendMoneyUseCase,
            FindAccountUseCase findAccountUseCase,
            UpdateAccountUseCase updateAccountUseCase) {
        return new AccountFacade(sendMoneyUseCase, findAccountUseCase, updateAccountUseCase);
    }
}
