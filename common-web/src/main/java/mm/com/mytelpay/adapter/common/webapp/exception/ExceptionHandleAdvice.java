package mm.com.mytelpay.adapter.common.webapp.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.dto.error.FieldErrorResponse;
import mm.com.mytelpay.adapter.common.dto.error.InvalidInputResponse;
import mm.com.mytelpay.adapter.common.dto.response.BaseResponse;
import mm.com.mytelpay.adapter.common.error.AuthorizationError;
import mm.com.mytelpay.adapter.common.error.BadRequestError;
import mm.com.mytelpay.adapter.common.error.ErrorCodeClient;
import mm.com.mytelpay.adapter.common.error.InternalServerError;
import mm.com.mytelpay.adapter.common.error.ResponseError;
import mm.com.mytelpay.adapter.common.exception.ForwardInnerAlertException;
import mm.com.mytelpay.adapter.common.exception.ResponseException;
import mm.com.mytelpay.adapter.common.webapp.i18n.LocaleStringService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
@Order
public class ExceptionHandleAdvice {

    private static final String HANDLE_FAILED_MESSAGE = "Failed to handle request ";
    private static final String INVALID_REQUEST_MESSAGE = "Invalid request arguments";
    private final LocaleStringService localeStringService;

    @Autowired
    public ExceptionHandleAdvice(LocaleStringService localeStringService) {
        this.localeStringService = localeStringService;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameter().getParameterName())
                        .message(e.getMessage())
                        .build());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalStateException(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn(
                "Failed to handle request file Upload"
                        + request.getRequestURI()
                        + ": "
                        + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<BaseResponse<Void>> handleMissingPathVariableException(
            MissingPathVariableException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameter().getParameterName())
                        .message("Missing path variable")
                        .build());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponse<Void>> handleMissingRequestHeaderException(
            MissingRequestHeaderException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameter().getParameterName())
                        .message("Missing request header")
                        .build());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Void>> handleRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getMethod())
                        .message("Http request method not support")
                        .build());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            ConstraintViolationException e, HttpServletRequest request) {
        String queryParam;
        String object;
        String errorMessage;
        Set<FieldErrorResponse> errors = new HashSet<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String queryParamPath = constraintViolation.getPropertyPath().toString();
            log.debug("queryParamPath = {}", queryParamPath);
            queryParam =
                    queryParamPath.contains(".")
                            ? queryParamPath.substring(queryParamPath.indexOf(".") + 1)
                            : queryParamPath;
            object =
                    queryParamPath.split("\\.").length > 1
                            ? queryParamPath.substring(
                                    queryParamPath.indexOf(".") + 1,
                                    queryParamPath.lastIndexOf("."))
                            : queryParamPath;
            errorMessage =
                    localeStringService.getMessage(
                            constraintViolation.getMessage(), constraintViolation.getMessage());
            errors.add(
                    FieldErrorResponse.builder()
                            .field(queryParam)
                            .objectName(object)
                            .message(errorMessage)
                            .build());
        }
        InvalidInputResponse invalidInputResponse;
        if (errors.isEmpty()) {
            long count = errors.size();
            invalidInputResponse =
                    errors.stream()
                            .skip(count - 1)
                            .findFirst()
                            .map(
                                    fieldErrorResponse ->
                                            new InvalidInputResponse(
                                                    HttpStatus.BAD_REQUEST.value(),
                                                    localeStringService.getMessage(
                                                            BadRequestError.INVALID_INPUT.getName(),
                                                            INVALID_REQUEST_MESSAGE),
                                                    fieldErrorResponse.getObjectName(),
                                                    errors))
                            .orElse(null);
        } else {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    INVALID_REQUEST_MESSAGE),
                            BadRequestError.INVALID_INPUT.getName(),
                            errors);
        }
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        setBaseResponseMessageFromError(baseResponse, invalidInputResponse);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            HttpMessageNotReadableException e, HttpServletRequest request) throws IOException {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        String message = "Invalid input format";
        Throwable cause = e.getCause();
        InvalidInputResponse invalidInputResponse;
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            String fieldPath =
                    invalidFormatException.getPath().stream()
                            .map(JsonMappingException.Reference::getFieldName)
                            .collect(Collectors.joining("."));

            // custom error with enum
            if (invalidFormatException.getTargetType() != null
                    && invalidFormatException.getTargetType().isEnum()) {
                message =
                        String.format(
                                "Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                                invalidFormatException.getValue(),
                                fieldPath,
                                Arrays.toString(
                                        invalidFormatException.getTargetType().getEnumConstants()));
            }
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    INVALID_REQUEST_MESSAGE),
                            BadRequestError.INVALID_INPUT.name(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(fieldPath)
                                            .message(message)
                                            .build()));
        } else if (cause instanceof JsonParseException) {
            JsonParseException jsonParseException = (JsonParseException) cause;
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    INVALID_REQUEST_MESSAGE),
                            BadRequestError.INVALID_INPUT.name(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(
                                                    jsonParseException
                                                            .getProcessor()
                                                            .getCurrentName())
                                            .message("Invalid input format")
                                            .build()));
        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mismatchedInputException = (MismatchedInputException) cause;
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    INVALID_REQUEST_MESSAGE),
                            BadRequestError.INVALID_INPUT.getName(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(
                                                    mismatchedInputException.getPath().stream()
                                                            .map(
                                                                    JsonMappingException.Reference
                                                                            ::getFieldName)
                                                            .collect(Collectors.joining(".")))
                                            .message("Mismatched input")
                                            .build()));
        } else if (cause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) cause;
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    INVALID_REQUEST_MESSAGE),
                            BadRequestError.INVALID_INPUT.getName(),
                            Collections.singleton(
                                    FieldErrorResponse.builder()
                                            .field(
                                                    jsonMappingException.getPath().stream()
                                                            .map(
                                                                    JsonMappingException.Reference
                                                                            ::getFieldName)
                                                            .collect(Collectors.joining(".")))
                                            .message("Json mapping invalid")
                                            .build()));
        } else {
            invalidInputResponse =
                    new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            localeStringService.getMessage(
                                    BadRequestError.INVALID_INPUT.getName(),
                                    INVALID_REQUEST_MESSAGE),
                            BadRequestError.INVALID_INPUT.getName(),
                            Collections.singleton(
                                    FieldErrorResponse.builder().message("Invalid input").build()));
        }

        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        setBaseResponseMessageFromError(baseResponse, invalidInputResponse);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<BaseResponse<Void>> handleResponseException(
            ResponseException e, HttpServletRequest request) {
        log.warn(
                "Failed to handle request {}: {}",
                request.getRequestURI(),
                e.getError().getMessage(),
                e);
        String message = e.getMessage();
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(message);
        baseResponse.setRefGatewayId(e.getRefGatewayId());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<BaseResponse<Void>> handleResponseException(
            InvocationTargetException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getMessage(), e);
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + error.getMessage(), e);
        String message = e.getMessage();
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        Set<FieldErrorResponse> fieldErrors =
                bindingResult.getAllErrors().stream()
                        .map(
                                objectError -> {
                                    try {
                                        FieldError fieldError = (FieldError) objectError;
                                        String message =
                                                localeStringService.getMessage(
                                                        fieldError.getDefaultMessage(),
                                                        fieldError.getDefaultMessage());
                                        return FieldErrorResponse.builder()
                                                .field(fieldError.getField())
                                                .objectName(fieldError.getObjectName())
                                                .message(message)
                                                .build();
                                    } catch (ClassCastException ex) {
                                        return null;
                                    }
                                })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        InvalidInputResponse invalidInputResponse =
                new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        localeStringService.getMessage(
                                BadRequestError.INVALID_INPUT.getName(), INVALID_REQUEST_MESSAGE),
                        BadRequestError.INVALID_INPUT.getName(),
                        fieldErrors);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.toString());
        setBaseResponseMessageFromError(baseResponse, invalidInputResponse);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<BaseResponse<Void>> handleResponseException(
            Exception e, HttpServletRequest request) {
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + error.getMessage(), e);
        String message =
                localeStringService.getMessage(
                        InternalServerError.INTERNAL_SERVER_ERROR.getName(),
                        "There are somethings wrong: {0}",
                        e);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler({
        DataIntegrityViolationException.class,
        NonTransientDataAccessException.class,
        DataAccessException.class
    })
    public ResponseEntity<BaseResponse<Void>> handleDataAccessException(
            DataAccessException e, HttpServletRequest request) {
        ResponseError error = InternalServerError.DATA_ACCESS_EXCEPTION;
        log.error(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + error.getMessage(), e);
        log.error(e.getMessage(), e);
        String message =
                localeStringService.getMessage(
                        InternalServerError.DATA_ACCESS_EXCEPTION.getName(),
                        "Data access exception",
                        e.getClass().getName());
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(
                FieldErrorResponse.builder()
                        .field(e.getParameterName())
                        .message(e.getMessage())
                        .build());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            MissingServletRequestPartException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        String message =
                localeStringService.getMessage(
                        BadRequestError.INVALID_INPUT.getName(), INVALID_REQUEST_MESSAGE);

        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            MultipartException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        String message =
                localeStringService.getMessage(
                        BadRequestError.INVALID_INPUT.getName(), INVALID_REQUEST_MESSAGE);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            BindException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        Set<FieldErrorResponse> fieldsErrors =
                e.getFieldErrors().stream()
                        .map(
                                fieldError ->
                                        FieldErrorResponse.builder()
                                                .field(fieldError.getField())
                                                .objectName(fieldError.getObjectName())
                                                .build())
                        .collect(Collectors.toSet());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, fieldsErrors);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            MismatchedInputException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);

        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder().message(e.getMessage()).build());
        String errorCode = ErrorCodeClient.FAIL.name();
        return createInvalidInputResponse(errorCode, errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            AccessDeniedException e, HttpServletRequest request) {
        log.warn(
                HANDLE_FAILED_MESSAGE
                        + request.getMethod()
                        + ": "
                        + request.getRequestURI()
                        + ": "
                        + e.getMessage(),
                e);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(AuthorizationError.ACCESS_DENIED.getName());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            InsufficientAuthenticationException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(
                "You were not authorized to request "
                        + request.getMethod()
                        + " "
                        + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            InternalAuthenticationServiceException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        return createAuthFailResponse(e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            BadCredentialsException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        return createAuthFailResponse(e);
    }

    private ResponseEntity<BaseResponse<Void>> createAuthFailResponse(Exception e) {
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @ExceptionHandler(ForwardInnerAlertException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationException(
            ForwardInnerAlertException e, HttpServletRequest request) {
        log.warn(HANDLE_FAILED_MESSAGE + request.getRequestURI() + ": " + e.getMessage(), e);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(ErrorCodeClient.FAIL.name());
        baseResponse.setMessage(e.getResponse().getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    private BaseResponse<Void> createBaseResponse() {
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    private void setBaseResponseMessageFromError(
            BaseResponse<Void> baseResponse, InvalidInputResponse invalidInputResponse) {
        if (Objects.nonNull(invalidInputResponse)
                && !CollectionUtils.isEmpty(invalidInputResponse.getErrors())) {
            String errorMessages =
                    invalidInputResponse.getErrors().stream()
                            .map(FieldErrorResponse::getMessage)
                            .collect(Collectors.joining(", "));
            baseResponse.setMessage(errorMessages);
        }
    }

    private ResponseEntity<BaseResponse<Void>> createInvalidInputResponse(
            String errorCode, Set<FieldErrorResponse> errors) {
        InvalidInputResponse invalidInputResponse =
                new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        localeStringService.getMessage(
                                BadRequestError.INVALID_INPUT.getName(), INVALID_REQUEST_MESSAGE),
                        BadRequestError.INVALID_INPUT.getName(),
                        errors);
        BaseResponse<Void> baseResponse = createBaseResponse();
        baseResponse.setErrorCode(errorCode);
        setBaseResponseMessageFromError(baseResponse, invalidInputResponse);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}
