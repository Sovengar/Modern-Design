package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._internal.DeletedRowService;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@WebAdapter("/api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
class DeleteAccountHttpController {
    private final DeleteAccount deleteAccount;

    @DeleteMapping(path = "/{accountNumber}/reason/{reason}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber, @PathVariable String reason) {
        log.info("BEGIN Controller - DeleteAccount");
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        deleteAccount.handle(accountNumber, reason);
        log.info("END Controller - DeleteAccount");
        return ResponseEntity.ok().build();
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
class DeleteAccount {
    private final AccountRepo repository;
    private final DeletedRowService deletedRowService;

    public void handle(final String accountNumber, final String reason) {
        log.info("BEGIN DeleteAccount");
        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        deletedRowService.saveDeletedEntity(account, "md.accounts", String.valueOf(account.accountId().id()), username, reason);
        repository.delete(accountNumber);

        log.info("END DeleteAccount");
    }
}
