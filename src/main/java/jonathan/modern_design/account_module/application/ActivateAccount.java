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
class ActivateAccountController {
    private final ActivateAccount activateAccount;

    public void activate(final ActivateAccount.Command message) {
        log.info("BEGIN Controller - ActiveAccount");
        activateAccount.handle(message);
        log.info("END Controller - ActiveAccount");
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
class ActivateAccount {
    private final AccountRepo repository;

    protected void handle(final Command message) {
        log.info("BEGIN - ActiveAccount");
        var account = repository.findOneOrElseThrow(message.accountNumber());
        account.activate();
        repository.update(account);
        log.info("END - ActiveAccount");
    }

    record Command(String accountNumber) {
    }

}
