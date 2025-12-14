# 4. Banking Module

Date: 2025-12-14

## Status

Accepted

## Context

The core value of the application lies in its banking capabilities. This domain is complex and likely to change frequently.

## Decision

We have encapsulated the core banking domain within the `banking` module.

Responsibilities:
- Account management (creation, updates, closure).
- Transaction processing (deposits, withdrawals, transfers).
- Balance calculations and history.

## Tactical Decisions

### Data Access

#### Decision
We use a hybrid approach for data access:
1.  **Spring Data JPA**: For standard CRUD operations and command-side persistence (writes).
2.  **QueryDSL**: For complex dynamic queries, reporting, and read-side operations where type safety is paramount.

#### Rationale
- **Spring Data JPA** vastly reduces boilerplate for standard repository interactions.

    ```java
    // Example: Standard Repository interface
    public interface AccountRepository extends JpaRepository<Account, Long> {
        Optional<Account> findByAccountNumber(String accountNumber);
    }
    ```

- **QueryDSL** provides a type-safe way to construct dynamic queries (e.g., transaction history with multiple filters) which is superior to string-based JPQL or Criteria API in terms of maintainability and compile-time checking.

    ```java
    // Example: Dynamic Query with QueryDSL
    public List<Transaction> findTransactions(LocalDate from, LocalDate to, BigDecimal minAmount) {
        QTransaction t = QTransaction.transaction;
        BooleanBuilder builder = new BooleanBuilder();
        
        if (from != null) builder.and(t.date.after(from));
        if (to != null) builder.and(t.date.before(to));
        if (minAmount != null) builder.and(t.amount.goe(minAmount));

        return queryFactory.selectFrom(t)
            .where(builder)
            .fetch();
    }
    ```


## Consequences

### Positive
- Core business logic is isolated from infrastructure and unrelated features.
- Domain experts can focus on this module without distraction.
- Clear boundaries for testing core rules.

### Negative
- Complex interactions with other modules (e.g., notifying users of transfers) require careful event/interface design.
