package com.cozisoft.starter.shared.core.errorhandling;

import com.cozisoft.starter.shared.core.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.web.error.include-stacktrace:always}")
    private String includeTrace;

    @ExceptionHandler(ManagerErrorException.class)
    public ResponseEntity<ManagerErrorResponse> handleManagerErrorExceptions(
            ManagerErrorException managerErrorException, WebRequest request) {

        log.info("Expected exception:", managerErrorException);

        boolean includeTechnicalDetails = includeTrace != null && includeTrace.equalsIgnoreCase("always");

        ManagerErrorResponse response = new ManagerErrorResponse();
        response.setTimestamp(Instant.now().toString());
        response.setStatus(managerErrorException.getStatusCode().value());
        response.setErrorCode(managerErrorException.getErrorCode().toString());
        response.setMessage(StringUtils.replacePlaceholders(managerErrorException.getTechnicalMessage(), managerErrorException.getErrorParameters()));
        response.setErrorParameters(managerErrorException.getErrorParameters());
        response.setPath(request.getDescription(false));
        if (includeTechnicalDetails) {
            response.setTrace(getFormattedStackTrace(managerErrorException));
        }

        return ResponseEntity
                .status(managerErrorException.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private String getFormattedStackTrace(Exception exception) {
        List<String> stackTrace = Stream.of(exception.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList());
        stackTrace.add(0, exception.getClass().getName() + " " + exception.getMessage());
        return String.join(" \n ", stackTrace);
    }
}
