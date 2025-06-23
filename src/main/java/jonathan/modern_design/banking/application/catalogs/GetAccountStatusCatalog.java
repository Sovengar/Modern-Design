package jonathan.modern_design.banking.application.catalogs;

import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.domain.models.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class GetAccountStatusCatalog {
    @GetMapping("/catalogs/status")
    public ResponseEntity<Response<List<CatalogEntry>>> getAccountStatusCatalog() {
        log.trace("BEGIN GetAccountStatusCatalog");
        var statusCatalog = Arrays.stream(Account.Status.values()).map(status -> new CatalogEntry(status.name(), status.name())).toList();
        log.trace("END GetAccountStatusCatalog");

        return ResponseEntity.ok().body(
                new Response.Builder<List<CatalogEntry>>()
                        .data(statusCatalog)
                        .withDefaultMetadataV1()
        );
    }

    record CatalogEntry(String name, String description) {
    }
}
