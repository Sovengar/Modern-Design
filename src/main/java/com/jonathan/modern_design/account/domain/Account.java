package com.jonathan.modern_design.account.domain;

import lombok.Builder;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
public class Account {
    AccountId id;
    AccountMoneyVO money;

    private Account(AccountId id, AccountMoneyVO money) {
        this.id = id;
        this.money = money;
    }

    //TODO FIX CURRENCY

    public static Account create(Long id, BigDecimal amount) {
        return new Account(AccountId.of(id), AccountMoneyVO.of(amount, "MXN"));
    }

    public Boolean isBalanceGreaterThan(BigDecimal anotherAmount) {
        return this.money.getAmount().compareTo(anotherAmount) >= 0;
    }

    public void add(BigDecimal amount) {
        this.money = this.money.add(AccountMoneyVO.of(amount, "MXN"));
    }

    public void subtract(BigDecimal amount) {
        this.money = this.money.subtract(AccountMoneyVO.of(amount, "MXN"));
    }

    //Evita en la medida de lo posible usar getters y setters
    public Long getId(){
        return id.getValue();
    }

}
