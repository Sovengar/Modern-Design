package com.jonathan.modern_design.config.exception;

import com.jonathan.modern_design.config.i18n.I18nUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.LazyInitializationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.BatchUpdateException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
class ExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String API_DEFAULT_ERROR_MESSAGE = "apiDefaultErrorMessage";
    private static final String API_DEFAULT_REQUEST_FAILED_MESSAGE = "apiDefaultRequestFailedMessage";
    private static final String VALIDATION_FIELDS_FAILED = "validationOfFieldsFailed";
    private static final String RECORD_NOT_FOUND = "recordNotFound";

    private final I18nUtils i18nUtils;

    // Process @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull final MethodArgumentNotValidException ex,
            @NonNull final HttpHeaders headers,
            @NonNull final HttpStatusCode status,
            @NonNull final WebRequest request) {
        log.info(ex.getMessage(), ex);

        final List<ErrorDetails> errors = new ArrayList<>();
        for (final ObjectError err : ex.getBindingResult().getAllErrors()) {
            errors.add(new ErrorDetails(((FieldError) err).getField(), err.getDefaultMessage()));
        }

        String validationFailedMessage = i18nUtils.getMessage(VALIDATION_FIELDS_FAILED, null, request.getLocale());
        ProblemDetail problemDetail = this.buildProblemDetail(BAD_REQUEST, validationFailedMessage, errors);
        return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
    }

    // Process controller method parameter validations e.g. @RequestParam, @PathVariable etc.
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            final @NotNull HandlerMethodValidationException ex,
            final @NotNull HttpHeaders headers,
            final @NotNull HttpStatusCode status,
            final @NotNull WebRequest request) {
        log.info(ex.getMessage(), ex);

        final List<ErrorDetails> errors = ex.getAllValidationResults().stream()
                .flatMap(validation -> validation.getResolvableErrors().stream()
                        .map(error -> new ErrorDetails(validation.getMethodParameter().getParameterName(), error.getDefaultMessage())))
                .collect(Collectors.toList());

        String validationFailedMessage = i18nUtils.getMessage(VALIDATION_FIELDS_FAILED, null, request.getLocale());
        return ResponseEntity.status(BAD_REQUEST)
                .body(this.buildProblemDetail(BAD_REQUEST, validationFailedMessage, errors));
    }

    // Process @Validated
    @ResponseStatus(BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ProblemDetail handleJakartaConstraintViolationException(
            final jakarta.validation.ConstraintViolationException ex, final WebRequest request) {
        log.info(ex.getMessage(), ex);

        final List<ErrorDetails> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorDetails(((PathImpl) violation.getPropertyPath()).getLeafNode().getName(), violation.getMessage()))
                .collect(Collectors.toList());

        String validationFailedMessage = i18nUtils.getMessage(VALIDATION_FIELDS_FAILED, null, request.getLocale());
        return this.buildProblemDetail(BAD_REQUEST, validationFailedMessage, errors);
    }

    @ResponseStatus(BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({
            org.hibernate.exception.ConstraintViolationException.class,
            DataIntegrityViolationException.class,
            BatchUpdateException.class,
            jakarta.persistence.PersistenceException.class,
            //PSQLException.class
    })
    public ProblemDetail handlePersistenceException(final Exception ex, final WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String cause = NestedExceptionUtils.getMostSpecificCause(ex).getLocalizedMessage();
        final String errorDetail = this.extractPersistenceDetails(cause);
        return this.buildProblemDetail(BAD_REQUEST, errorDetail);
    }

    /*
     *  When authorizing user at controller or service layer using @PreAuthorize it throws
     * AccessDeniedException, and it's a developer's responsibility to catch it
     * */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(final Exception ex, final WebRequest request) {
        log.info(ex.getMessage(), ex);
        return this.buildProblemDetail(HttpStatus.FORBIDDEN, null);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyResultDataAccessException.class)
    public ProblemDetail handleEmptyResultDataAccessException(
            final EmptyResultDataAccessException ex, final WebRequest request) {
        log.info(ex.getMessage(), ex);

        String message = i18nUtils.getMessage(RECORD_NOT_FOUND, null, request.getLocale());
        return this.buildProblemDetail(HttpStatus.NOT_FOUND, message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(LazyInitializationException.class)
    public ProblemDetail handleLazyInitialization(final LazyInitializationException ex, final WebRequest request) {
        UUID errorId = UUID.randomUUID();
        log.warn("Error ID: {} - {}", errorId, ex.getMessage(), ex);

        // this.slack.notify(format("LazyInitializationException: %s", ex.getMessage()));

        Locale currentLocale = LocaleContextHolder.getLocale();
        String message = i18nUtils.getMessage(API_DEFAULT_ERROR_MESSAGE, null, currentLocale);
        message += " Error ID: " + errorId;

        return this.buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    //Catch API defined exceptions
    @org.springframework.web.bind.annotation.ExceptionHandler(RootException.class)
    public ResponseEntity<ProblemDetail> handleCustomExceptions(final RootException ex) {
        log.info(ex.getMessage(), ex);

        // if (ex.getHttpStatus().is5xxServerError()) {
        //   this.slack.notify(format("[API] InternalServerError: %s", ex.getMessage()));
        // }

        Locale currentLocale = LocaleContextHolder.getLocale();
        String message = org.springframework.util.StringUtils.hasText(ex.getMessageToBeDisplayed()) ?
                i18nUtils.getMessage(ex.getMessageToBeDisplayed(), null, currentLocale) :
                i18nUtils.getMessage(API_DEFAULT_REQUEST_FAILED_MESSAGE, null, currentLocale);

        final ProblemDetail problemDetail = this.buildProblemDetail(ex.getHttpStatus(), message, ex.getErrors());
        return ResponseEntity.status(ex.getHttpStatus()).body(problemDetail);
    }

    //Fallback, catch all unknown exceptions
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public ResponseEntity<ProblemDetail> handleUnknownExceptions(final Throwable ex) {
        UUID errorId = UUID.randomUUID();
        log.warn("Error ID: {} - {}", errorId, ex.getMessage(), ex);

        // this.slack.notify(format("[API] InternalServerError: %s", ex.getMessage()));

        Locale currentLocale = LocaleContextHolder.getLocale();
        String message = i18nUtils.getMessage(API_DEFAULT_ERROR_MESSAGE, null, currentLocale);
        message += " Error ID: " + errorId;

        final ProblemDetail problemDetail = this.buildProblemDetail(INTERNAL_SERVER_ERROR, message);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(problemDetail);
    }


    private ProblemDetail buildProblemDetail(final HttpStatus status, final String detail) {
        return this.buildProblemDetail(status, detail, emptyList());
    }

    private ProblemDetail buildProblemDetail(final HttpStatus status, final String detail, final List<ErrorDetails> errors) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, StringUtils.normalizeSpace(detail));

        if (!errors.isEmpty()) {
            problemDetail.setProperty("errors", errors);
        }

        problemDetail.setProperty("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(LocalDateTime.now()));
        problemDetail.setProperty("errorId", UUID.randomUUID().toString());

        return problemDetail;
    }

    private String extractPersistenceDetails(final String cause) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String details = i18nUtils.getMessage(API_DEFAULT_ERROR_MESSAGE, null, currentLocale);

        // Example: ERROR: duplicate key value violates unique constraint "company_slug_key"  Detail:
        // Key (slug)=(bl8lo0d) already exists.
        if (cause.contains("Detail")) {
            final List<String> matchList = new ArrayList<>();
            // find database values between "()"
            final Pattern pattern = Pattern.compile("\\((.*?)\\)");
            final Matcher matcher = pattern.matcher(cause);

            // Creates list ["slug", "bl8lo0d"]
            while (matcher.find()) {
                matchList.add(matcher.group(1));
            }

            if (matchList.size() == 2) {
                final String key = matchList.get(0);
                final String value = matchList.get(1);
                // Gets the message after the last ")"
                final String message = cause.substring(cause.lastIndexOf(")") + 1);

                // return errorMessage: slug 'bl8lo0d'  already exists.
                details = format("%s '%s' %s", key, value, message);
            }
        }

        return details;
    }
}


