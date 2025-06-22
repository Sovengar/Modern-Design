package jonathan.modern_design.auth.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.domain.tags.ApplicationService;
import jonathan.modern_design._shared.domain.tags.WebAdapter;
import jonathan.modern_design._shared.infra.config.exception.RootException;
import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.models.Role;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.RoleStore;
import jonathan.modern_design.auth.domain.store.UserRepo;
import jonathan.modern_design.auth.domain.vo.UserEmail;
import jonathan.modern_design.auth.domain.vo.UserName;
import jonathan.modern_design.auth.domain.vo.UserPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serial;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/users")
class RegisterUserController {
    private final RegisterUser handler;
    private final UserRepo repository;

    @Operation(summary = "RegisterUser")
    @PostMapping
    public ResponseEntity<Response<DataResponse>> registerUser(final @Valid @RequestBody Request request) {
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN RegisterUser for userId: {}", request.id());
        var command = new RegisterUser.Command(request.id(), Optional.ofNullable(request.realname()), request.username(), request.email(), request.password(), request.country(), request.phoneNumbers());
        var userId = handler.handle(command);
        var user = repository.findById(User.Id.of(userId)).orElseThrow();
        log.info("END RegisterUser for userId: {}", request.id());

        var roleDto = new DataResponse.RoleDto(
                user.getRole().getCode().getRoleCode(),
                user.getRole().getDescription(),
                List.of(new Response.Link("findRole", "/roles/" + user.getRole().getCode().getRoleCode(), "GET"))
        );

        return ResponseEntity.ok(
                new Response.Builder<DataResponse>()
                        .data(new DataResponse(userId, roleDto))
                        .links(List.of(new Response.Link("findUser", "/users/" + userId, "GET")))
                        .actions(List.of(
                                //new Response.Action("changeRoleTo", "/users/" + userId + "/changeRoleTo/" + Roles.USER.getCode(), "PUT"),
                                new Response.Action("deleteUser", "/users/" + userId, "DELETE")
                        )) //Smelly… We have to go back to this and keep adding here.
                        .withDefaultMetadataV1()
        );
    }

    record DataResponse(UUID userId, RoleDto role) {
        record RoleDto(String code, String name, List<Response.Link> links) {
        }
    }

    public record Request(
            @NotNull(message = "User id must not be null")
            UUID id,
            String realname,
            @NotEmpty(message = "User username must not be empty")
            String username,
            @NotEmpty(message = "User email must not be empty")
            String email,
            @NotEmpty(message = "User password must not be empty")
            String password,
            @NotEmpty(message = "User country must not be empty")
            String country,
            @NotEmpty(message = "User phone numbers must not be empty")
            List<String> phoneNumbers
    ) {
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

        //Begin with Complex logic to know the final role of the auth
        var roleCode = Role.Code.of(Roles.USER.getCode());
        var role = roleStore.findByCode(roleCode);
        //End of complex logic

        //Complex logic to decide the auth
        var user = User.Factory.register(
                User.Id.of(message.id()),
                UserName.of(message.username()),
                UserEmail.of(message.email()),
                UserPassword.of(message.password()),
                role
        );
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
            @NotNull(message = "User id must not be null")
            UUID id,
            Optional<String> realname,
            @NotEmpty(message = "User username must not be empty")
            String username,
            @NotEmpty(message = "User email must not be empty")
            String email,
            @NotEmpty(message = "User password must not be empty")
            String password,
            @NotEmpty(message = "User country must not be empty")
            String country,
            @NotEmpty(message = "User phone numbers must not be empty")
            List<String> phoneNumbers
    ) {
    }
}

