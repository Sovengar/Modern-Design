package jonathan.modern_design.account_module.infra;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.AccountApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class AccountController {
    private final AccountApi accountFacade;

    @GetMapping(path = "/{password}/user")
    public AccountDto getAcc(@PathVariable String password) {
        return accountFacade.findByUserPassword(password).orElseThrow(EntityNotFoundException::new);
    }


}
