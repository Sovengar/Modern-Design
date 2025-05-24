package jonathan.modern_design._internal.config.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    // Process @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull final MethodArgumentNotValidException ex,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request) {
        final List<ErrorDetails> errors = new ArrayList<>();
        for (final ObjectError err : ex.getBindingResult().getAllErrors()) {
            errors.add(new ErrorDetails(((FieldError) err).getField(), err.getDefaultMessage()));
        }

        var problemDetail = this.buildProblemDetail(BAD_REQUEST, "Validation Failed", errors);
        log.debug("Error ID: {} - {}", getErrorId(problemDetail), ex.getMessage(), ex);

        return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
    }

    // Process controller method parameter validations e.g. @RequestParam, @PathVariable etc.
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            final @NotNull HandlerMethodValidationException ex,
            final @NotNull HttpHeaders headers,
            final @NotNull HttpStatusCode status,
            final @NotNull WebRequest request) {
        final List<ErrorDetails> errors = ex.getAllValidationResults().stream()
                .flatMap(validation -> validation.getResolvableErrors().stream()
                        .map(error -> new ErrorDetails(validation.getMethodParameter().getParameterName(), error.getDefaultMessage())))
                .collect(Collectors.toList());

        var problemDetail = this.buildProblemDetail(BAD_REQUEST, "Validation Failed", errors);
        log.debug("Error ID: {} - {}", getErrorId(problemDetail), ex.getMessage(), ex);

        return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
    }

    // Process @Validated
    @ExceptionHandler
    public ProblemDetail handleJakartaConstraintViolationException(final ConstraintViolationException ex, final WebRequest request) {
        final List<ErrorDetails> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorDetails(((PathImpl) violation.getPropertyPath()).getLeafNode().getName(), violation.getMessage()))
                .collect(Collectors.toList());

        var problemDetail = this.buildProblemDetail(BAD_REQUEST, "Validation Failed", errors);
        log.error("Error ID: {} - {}", getErrorId(problemDetail), ex.getMessage(), ex);

        return problemDetail;
    }

    //Catch API defined exceptions
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleCustomExceptions(final RootException ex) {

        final ProblemDetail problemDetail = this.buildProblemDetail(ex.getHttpStatus(), ex.getMessage(), ex.getErrors());
        log.error("Error ID: {} - {}", getErrorId(problemDetail), ex.getMessage(), ex);

        return ResponseEntity.status(ex.getHttpStatus()).body(problemDetail);
    }

    //Fallback, catch all unknown exceptions
    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleUnknownExceptions(final Throwable ex) {
        final var problemDetail = this.buildProblemDetail(INTERNAL_SERVER_ERROR, "Unexpected error");
        log.error("Error ID: {} - {}", getErrorId(problemDetail), ex.getMessage(), ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    private String getErrorId(final ProblemDetail problemDetail) {
        var errorId = problemDetail.getProperties().get("errorId").toString();
        assert errorId != null;

        return errorId;
    }

    private ProblemDetail buildProblemDetail(final HttpStatus status, final String detail) {
        return this.buildProblemDetail(status, detail, emptyList());
    }

    private ProblemDetail buildProblemDetail(final HttpStatus status, final String detail, final List<ErrorDetails> errors) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, StringUtils.normalizeSpace(detail));
        problemDetail.setDetail(detail);

        if (!errors.isEmpty()) {
            problemDetail.setProperty("errors", errors);
        }

        problemDetail.setProperty("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(LocalDateTime.now()));
        problemDetail.setProperty("errorId", UUID.randomUUID().toString());

        return problemDetail;
    }
}


