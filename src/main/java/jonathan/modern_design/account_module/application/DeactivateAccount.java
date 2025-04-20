package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.domain.repos.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@WebAdapter
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
class DeactivateAccountController {
    private final DeactivateAccount deactivateAccount;

    public void deactivate(final DeactivateAccount.Command message) {
        log.info("BEGIN Controller - DeactiveAccount");
        deactivateAccount.handle(message);
        log.info("END Controller - DeactiveAccount");
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
class DeactivateAccount {
    private final AccountRepo repository;

    protected void handle(final Command message) {
        log.info("BEGIN - DeactiveAccount");
        var account = repository.findOneOrElseThrow(message.accountNumber());
        account.deactivate();
        repository.update(account);
        log.info("END - DeactiveAccount");

    }

    record Command(String accountNumber) {
    }

}
