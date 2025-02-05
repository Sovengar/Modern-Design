package com.jonathan.modern_design.account_module.infraestructure.context;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.AccountMapperAdapter;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountService;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountService;
import com.jonathan.modern_design.account_module.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.send_money.SendMoneyService;
import com.jonathan.modern_design.account_module.application.send_money.SendMoneyUseCase;
import com.jonathan.modern_design.account_module.application.update_account.UpdateAccountService;
import com.jonathan.modern_design.account_module.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.services.AccountValidator;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountRepositoryFake;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountRepositorySpringAdapter;
import com.jonathan.modern_design.account_module.infraestructure.persistence.SpringAccountRepository;
import com.jonathan.modern_design.user_module.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfigurationFactory {

    @Bean
    public AccountRepository accountRepository(SpringAccountRepository repository) {
        AccountMapperAdapter accountMapperAdapter = new AccountMapperAdapter();
        return new AccountRepositorySpringAdapter(repository, accountMapperAdapter);
    }

    @Bean
    public SendMoneyUseCase sendMoneyUseCase(FindAccountUseCase findAccountUseCase, UpdateAccountUseCase updateAccountUseCase) {
        AccountValidator accountValidator = new AccountValidator();

        return new SendMoneyService(findAccountUseCase, updateAccountUseCase, accountValidator);
    }

    @Bean
    public FindAccountUseCase findAccountUseCase(AccountRepository accountRepository) {
        return new FindAccountService(accountRepository);
    }

    @Bean
    public UpdateAccountUseCase updateAccountUseCase(AccountRepository accountRepository) {
        return new UpdateAccountService(accountRepository);
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository, UserFacade userFacade) {
        return new CreateAccountService(accountRepository, userFacade);
    }

    @Bean
    public AccountFacade accountFacade(AccountRepository accountRepository, UserFacade userFacade) {
        UpdateAccountUseCase updateAccountUseCase = updateAccountUseCase(accountRepository);
        SendMoneyUseCase sendMoneyUseCase = sendMoneyUseCase(findAccountUseCase(accountRepository), updateAccountUseCase);
        CreateAccountUseCase createAccountUseCase = createAccountUseCase(accountRepository, userFacade);

        return new AccountFacade(accountRepository, sendMoneyUseCase, updateAccountUseCase, createAccountUseCase);
    }

    public AccountFacade accountFacade(UserFacade userFacade) {
        return accountFacade(new AccountRepositoryFake(), userFacade);
    }



/*
    @Bean
    fun getShoeService(jdbcTemplate: JdbcTemplate): ShoeService {
        // an example of 'hiding' the details implementation, only the shoeservice can be grabbed via DI
        return ShoeService(PostgresShoeRepository(jdbcTemplate))
    }

    @Bean
    fun getProductVariantService(jdbcTemplate: JdbcTemplate, jedis: Jedis): ProductVariantService {
        return ProductVariantService(PostgresProductVariantRepository(jdbcTemplate))
    }

    @Bean
    fun getInventoryManagementService(jdbcTemplate: JdbcTemplate, jedis: Jedis) {
        return InventoryManagementService(PostgresProductVariantRepository(jdbcTemplate), RedisInventoryWarehousingRepository(jedis);
    }

    @Bean
    fun getOrderRepository(jdbcTemplate: JdbcTemplate, dynamoDbClient: DynamoDbClient): OrderRepository {
        return PostgresOrderRepository(jdbcTemplate)
//        return DynamoDbOrderRepository(dynamoDbClient)
    }
    */
}
