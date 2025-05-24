package jonathan.modern_design.account_module.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jonathan.modern_design._common.api.Response;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountNumber;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class GenericUpdateAccountHttpController {
    private final GenericUpdateAccount updater;

    @Observed(name = "updateAccount")
    @Operation(description = "Update Account")
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response<Void>> updateAccount(@RequestBody UpdateAccountRequestDto requestDto) {
        generateTraceId();

        log.info("BEGIN Updating account with number {} with this JSON: {}", requestDto.accountNumber(), requestDto);
        var accountDto = new AccountDto(
                requestDto.accountNumber(),
                requestDto.balance(),
                requestDto.currency(),
                null,
                Account.Status.valueOf(requestDto.status()),
                null
        );

        updater.handle(accountDto);
        log.info("END Account with number {} updated", requestDto.accountNumber());

        return ResponseEntity.ok().body(new Response.Builder<Void>().data(null).withDefaultMetadata().build());
    }

    @Schema(description = "Data for updating an account")
    record UpdateAccountRequestDto(
            @Schema(description = "Account number", example = "1234567890")
            @NotBlank(message = "Account number is required")
            String accountNumber,

            @Schema(description = "Account balance", example = "1000.50")
            @DecimalMin(value = "0.0", message = "Balance cannot be negative")
            BigDecimal balance,

            @Schema(description = "Account currency", example = "USD")
            @NotBlank(message = "Currency is required")
            String currency,

            @Schema(description = "Account status", example = "ACTIVE")
            String status,

            @Schema(description = "Account type", example = "SAVINGS")
            String accountType
    ) {
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class GenericUpdateAccount {
    private final AccountRepo repository;

    /**
     * CRUD method, prefer usecase methods like moveToAnotherPlace to update the address only.
     * If we keep making the method more generic, the method will grow complex.
     * Logic will be dispersed since the client now has the burden to provide the right fields to support his need.
     */
    @Transactional
    public void handle(AccountDto dto) {
        log.info("BEGIN Update");

        var account = repository.findByAccNumberOrElseThrow(dto.accountNumber());
        account.genericUpdate(
                AccountNumber.of(dto.accountNumber()),
                AccountMoney.of(dto.balance(), Currency.valueOf(dto.currency())),
                AccountAddress.of(dto.address()),
                dto.status(),
                dto.userId()
        );

        repository.update(account);

        log.info("END Update");
    }
}
