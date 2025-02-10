package com.jonathan.modern_design.account_module.infraestructure.persistence;

import com.jonathan.modern_design.account_module.application.AccountMapper;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.shared.Currency;
import com.jonathan.modern_design.shared.annotations.PersistenceAdapter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountRepositorySpringAdapter implements AccountRepository { //TODO FIX, AQUI DEBERIA LLAMAR LAS INTERFACES, YA QUE ALOMEJOR LA ESCRITURA ES DB Y LA LECTURA ES DE UN JSON
    private final SpringAccountRepository repository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(accountMapper::toAccount);
    }

    @Override
    public Page<Account> findAll(final Pageable pageable) {
        List<Account> accounts = repository.findAll(pageable)
                .getContent()
                .stream()
                .map(accountMapper::toAccount)
                .toList();

        return new PageImpl<>(accounts, pageable, accounts.size());
    }

    @Override
    public Account create(Account account) {
        var accountEntity = accountMapper.toAccountEntity(account);
        accountEntity = repository.save(accountEntity);
        return accountMapper.toAccount(accountEntity);
    }

    @Override
    public void update(Account account) {
        repository.save(accountMapper.toAccountEntity(account));
    }

    @Override
    public void delete(final String accountNumber) {
        repository.deleteById(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        this.findOneEntity(accountNumber).ifPresent(account -> {
            account.setDeleted(true);
            repository.save(account);
        });
    }

    @Override
    public void deposit(final String accountNumber, final BigDecimal amount, final Currency currency) {
        //TODO
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repository.findById(accountNumber);
    }

}
