package jonathan.modern_design.account_module.dtos;

public record CreateAccountCommand(String realname, String email, String username, String address, String password,
                                   String country,
                                   String currency) {
}
