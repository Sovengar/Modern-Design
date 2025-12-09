package jonathan.modern_design.search.internal;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
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

import static jonathan.modern_design._shared.infra.AppUrls.SearchUrls.SEARCH_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.SearchUrls.XXX_RESOURCE_URL;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(SEARCH_MODULE_URL + XXX_RESOURCE_URL)
class IdkHttpController {
    private final IdkSearch querier;

    @Operation(description = "Search Account")
    @PostMapping
    public ResponseEntity<Response<List<AccountDto>>> searchForXXXPage(@RequestBody IdkSearch.AccountCriteria filters) {
        var accountDtos = querier.searchForXXXPage(filters);
        return ResponseEntity.ok(new Response.Builder<List<AccountDto>>().data(accountDtos).withDefaultMetadataV1());
    }

    @Operation(description = "Search Account")
    @GetMapping(path = "/byuser/password/{password}")
    public ResponseEntity<Response<AccountDto>> findAccount(@PathVariable String password) {
        var accountDto = querier.searchWithUserPassword(password).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new Response.Builder<AccountDto>().data(accountDto).withDefaultMetadataV1());
    }

    @GetMapping(path = "/byuser/{userId}")
    public ResponseEntity<Response<AccountWithUserInfo>> findAccount(@PathVariable UUID userId) {
        var viewModel = querier.findAccountWithUserInfo(userId).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new Response.Builder<AccountWithUserInfo>().data(viewModel).withDefaultMetadataV1());
    }
}
