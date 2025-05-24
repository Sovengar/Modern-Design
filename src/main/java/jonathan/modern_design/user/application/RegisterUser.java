package jonathan.modern_design.user.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._common.api.Response;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design._internal.config.exception.RootException;
import jonathan.modern_design._shared.country.Country;
import jonathan.modern_design.user.domain.catalogs.Roles;
import jonathan.modern_design.user.domain.models.Role;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.models.vo.UserEmail;
import jonathan.modern_design.user.domain.models.vo.UserPassword;
import jonathan.modern_design.user.domain.models.vo.UserPhoneNumbers;
import jonathan.modern_design.user.domain.models.vo.UserRealName;
import jonathan.modern_design.user.domain.models.vo.UserUserName;
import jonathan.modern_design.user.domain.store.RoleStore;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.Serial;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/users")
class RegisterUserController {
    private final RegisterUser handler;
    private final UserRepo repository;

    @Observed(name = "registerUser")
    @Operation(summary = "RegisterUser")
    @PostMapping
    public ResponseEntity<Response<DataResponse>> registerUser(final @Valid RegisterUser.Command request) {
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN RegisterUser for userId: {}", request.id());
        var userId = handler.handle(request);
        var user = repository.findById(User.Id.of(userId)).orElseThrow();
        log.info("END RegisterUser for userId: {}", request.id());

        var roleDto = new DataResponse.RoleDto(
                user.getRole().getCode().getRoleCode(),
                user.getRole().getDescription(),
                List.of(new Response.Link("findRole", "/roles/" + user.getRole().getCode(), "GET"))
        );

        return ResponseEntity.ok(
                new Response.Builder<DataResponse>()
                        .data(new DataResponse(userId, roleDto))
                        .links(List.of(new Response.Link("findUser", "/users/" + userId, "GET")))
                        .actions(List.of(
                                //new Response.Action("changeRoleTo", "/users/" + userId + "/changeRoleTo/" + Roles.USER.getCode(), "PUT"),
                                new Response.Action("deleteUser", "/users/" + userId + "/changeRoleTo/" + userId, "DELETE")
                        ))
                        .withDefaultMetadataV1()
        );
    }

    record DataResponse(UUID userId, RoleDto role) {
        record RoleDto(String code, String name, List<Response.Link> links) {
        }
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
@Transactional
public class RegisterUser {
    private final UserRepo repository;
    private final RoleStore roleStore;

    public UUID handle(final @Valid Command message) {
        log.info("BEGIN RegisterUser");

        repository.findById(User.Id.of(message.id())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", message.id()));
        });

        //Begin with Complex logic to know the final role of the user
        var roleCode = Role.Code.of(Roles.USER.getCode());
        var role = roleStore.findByCode(roleCode);
        //End of complex logic

        //Complex logic to decide the user
        var user = User.Factory.register(User.Id.of(message.id()), UserRealName.of(message.realname().orElse("")), UserUserName.of(message.username()), UserEmail.of(message.email()), UserPassword.of(message.password()), message.country(), UserPhoneNumbers.of(message.phoneNumbers()), role);
        repository.registerUser(user);

        log.info("END RegisterUser");
        return user.getId().getUserId();
    }

    private static class UserAlreadyExistsException extends RootException {
        @Serial private static final long serialVersionUID = 1604523616703390261L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public record Command(
            @NotNull
            UUID id,
            Optional<String> realname,
            @NotEmpty
            String username,
            @NotEmpty
            String email,
            @NotEmpty
            String password,
            @NotEmpty
            Country country,
            @NotEmpty
            List<String> phoneNumbers
    ) {
    }
}

