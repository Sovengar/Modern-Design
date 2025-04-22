package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class DeactivateAccountController {
    private final DeactivateAccount deactivateAccount;

    @PutMapping(path = "/{accountNumber}/deactivate")
    public ResponseEntity<Void> deactivate(final @PathVariable String accountNumber) {
        log.info("BEGIN Controller - DeactiveAccount");
        deactivateAccount.handle(new DeactivateAccount.Command(accountNumber));
        log.info("END Controller - DeactiveAccount");
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class DeactivateAccount {
    private final AccountRepo repository;

    protected void handle(final @Valid Command message) {
        log.info("BEGIN - DeactiveAccount");
        var account = repository.findOneOrElseThrow(message.accountNumber());
        account.deactivate();
        repository.update(account);
        log.info("END - DeactiveAccount");

    }

    record Command(@NotEmpty(message = "Account number is required") String accountNumber) {
    }

}
