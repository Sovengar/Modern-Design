package jonathan.modern_design.banking.queries;

import jonathan.modern_design.__config.runners.MockedWebRunner;
import jonathan.modern_design._utils.CommonWebHelper;
import jonathan.modern_design._utils.CustomResourceLoader;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static jonathan.modern_design.banking.domain.AccountDsl.DEFAULT_ACCOUNT_NUMBER;
import static jonathan.modern_design.banking.domain.AccountDsl.givenARandomAccountWithBalance;
import static jonathan.modern_design.banking.domain.AccountDsl.givenAnEmptyAccount;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@MockedWebRunner
@WebMvcTest(controllers = FindAccountHttpController.class)
@Import({CommonWebHelper.class, CustomResourceLoader.class})
class FindAccountWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindAccount findAccount;

    @Autowired
    private CommonWebHelper helper;

    @Test
    void should_find_empty_account_by_account_number() throws Exception {
        // Arrange
        var account = givenAnEmptyAccount();
        givenARandomAccountWithBalance(10.0);

        when(findAccount.queryWith(DEFAULT_ACCOUNT_NUMBER)).thenReturn(Optional.of(new AccountDto(account)));

        // Act & Assert
        mockMvc.perform(get("/banking/v1/accounts/" + DEFAULT_ACCOUNT_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(helper.validJson("./responses/banking/queries/find-account-response.json"));
    }
}
