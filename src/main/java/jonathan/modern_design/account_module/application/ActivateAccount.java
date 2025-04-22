package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class ActivateAccountController {
    private final ActivateAccount activateAccount;

    @PutMapping(path = "/{accountNumber}/activate")
    public ResponseEntity<Void> activate(final @PathVariable String accountNumber) {
        log.info("BEGIN Controller - ActivateAccount");
        activateAccount.handle(new ActivateAccount.Command(accountNumber));
        log.info("END Controller - ActivateAccount");
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
@Validated
class ActivateAccount {
    private final AccountRepo repository;

    protected void handle(final @Valid Command message) {
        log.info("BEGIN - ActivateAccount");
        var account = repository.findOneOrElseThrow(message.accountNumber());
        account.activate();
        repository.update(account);
        log.info("END - ActivateAccount");
    }

    record Command(@NotEmpty(message = "Account number is required") String accountNumber) {
    }

}
