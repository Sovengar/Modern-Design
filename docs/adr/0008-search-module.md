# 8. Search Module

Date: 2025-12-14

## Status

Accepted

## Context

Efficient searching often requires specialized indexing or distinct query models (CQRS) that differ from transactional write models.

## Decision

We have placed search capabilities in the `search` module.

Responsibilities:
- Indexing data for fast retrieval.
- providing search APIs/interfaces.
- integration with search engines (e.g., Elasticsearch) if applicable.

## Tactical Decisions

### Query Construction

#### Decision
We utilize **QueryDSL** as the primary mechanism for constructing search queries.

#### Rationale
Search requirements often involve dynamic combinations of filters (e.g., searching by date range, amount, category, status).
- **Type Safety**: QueryDSL allows us to build these queries safely based on the domain model.
- **Flexibility**: It handles optional parameters and dynamic predicates gracefully, avoiding "if-else" spaghetti code in string concatenation.

    ```java
    // Example: Constructing a search predicate
    public Predicate buildSearchPredicate(SearchCriteria criteria) {
        QProduct p = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.getName() != null) {
            builder.and(p.name.containsIgnoreCase(criteria.getName()));
        }

        if (criteria.getCategory() != null) {
            builder.and(p.category.eq(criteria.getCategory()));
        }

        if (criteria.isInStockOnly()) {
            builder.and(p.stock.gt(0));
        }

        return builder;
    }
    ```


## Consequences

### Positive
- Read optimizations do not complicate the write model.
- Can scale read workloads independently.

### Negative
- Data Consistency: There may be a lag between writing data and it becoming searchable (Eventual Consistency).
