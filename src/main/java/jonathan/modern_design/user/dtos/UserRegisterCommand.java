package jonathan.modern_design.user.dtos;

import jonathan.modern_design._shared.country.Country;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record UserRegisterCommand(UUID uuid, Optional<String> realname, String username, String email, String password,
                                  Country country, List<String> phoneNumbers) {
}
