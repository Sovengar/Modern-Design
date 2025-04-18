package jonathan.modern_design.account_module.infra;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.dtos.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class AccountController {
    private final AccountApi accountFacade;


    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountDto> loadAccount(@PathVariable String accountNumber) {
        return ok(accountFacade.findOne(accountNumber));
    }

    //@Operation(description = "Search Account")
    @PostMapping("/search")
    public List<AccountDto> search(@RequestBody AccountSearchRepo.AccountSearchCriteria searchCriteria) {
        return accountFacade.search(searchCriteria);
    }

    @GetMapping(path = "/{password}/user")
    public AccountDto getAcc(@PathVariable String password) {
        return accountFacade.findByUserPassword(password).orElseThrow(EntityNotFoundException::new);
    }


}
