package jonathan.modern_design.search.internal;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.queries.SearchAccount;
import jonathan.modern_design.search.view_models.AccountWithUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/search")
class IdkHttpController {
    private final IdkSearch querier;

    @Operation(description = "Search Account")
    @PostMapping("/xxx")
    public ResponseEntity<Response<List<AccountDto>>> searchForXXXPage(@RequestBody SearchAccount.Criteria filters) {
        var accountDtos = querier.searchForXXXPage(filters);
        return ResponseEntity.ok(new Response.Builder<List<AccountDto>>().data(accountDtos).withDefaultMetadataV1());
    }

    @Operation(description = "Search Account")
    @GetMapping(path = "/xxx/byuser/password/{password}")
    public ResponseEntity<Response<AccountDto>> findAccount(@PathVariable String password) {
        var accountDto = querier.searchWithUserPassword(password).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new Response.Builder<AccountDto>().data(accountDto).withDefaultMetadataV1());
    }

    @GetMapping(path = "/xxx/byuser/{userId}")
    public ResponseEntity<Response<AccountWithUserInfo>> findAccount(@PathVariable UUID userId) {
        var viewModel = querier.findAccountWithUserInfo(userId).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new Response.Builder<AccountWithUserInfo>().data(viewModel).withDefaultMetadataV1());
    }
}
