package jonathan.modern_design.banking.queries;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.domain.CountryRepo;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.Transaction;
import jonathan.modern_design.banking.domain.vo.PersonalId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.CATALOGS_URL;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(BANKING_MODULE_URL + CATALOGS_URL)
class GetCatalogsBanking {
    private final CountryRepo countryRepo;

    @Operation(summary = "Get all account status")
    @GetMapping(value = "/accountStatus")
    public ResponseEntity<Response<List<Row>>> getAccountStatus() {
        var data = Arrays.stream(Account.AccountStatus.values()).map(status -> new Row(status.name(), status.name(), status.name())).toList();
        return ResponseEntity.ok().body(new Response.Builder<List<Row>>().data(data).withDefaultMetadataV1());
    }

    @Operation(summary = "Get all transaction types")
    @GetMapping(value = "/transactionTypes")
    public ResponseEntity<Response<List<Row>>> getTransactionTypes() {
        var data = Arrays.stream(Transaction.TransactionType.values()).map(type -> new Row(type.name(), type.name(), type.name())).toList();
        return ResponseEntity.ok().body(new Response.Builder<List<Row>>().data(data).withDefaultMetadataV1());
    }

    @Operation(summary = "Get all personal id types")
    @GetMapping(value = "/personalIdTypes")
    public ResponseEntity<Response<List<Row>>> getPersonalIdTypes() {
        var data = Arrays.stream(PersonalId.PersonalIdType.values()).map(type -> new Row(type.name(), type.name(), type.name())).toList();
        return ResponseEntity.ok().body(new Response.Builder<List<Row>>().data(data).withDefaultMetadataV1());
    }

    @Operation(summary = "Get all countries from banking perspective")
    @GetMapping(value = "/countries")
    public ResponseEntity<Response<List<Row>>> getCountries() {
        var countries = countryRepo.countries(); //We could add a domain rule, like only European countries are allowed

        var data = countries.stream().map(country -> new Row(country.code(), country.code(), country.name())).toList();
        return ResponseEntity.ok().body(new Response.Builder<List<Row>>().data(data).withDefaultMetadataV1());
    }

    public record Row(String key, String value, String description) {
    }
}
