package com.cozisoft.starter.shared.core.errorhandling;

import com.cozisoft.starter.shared.core.StringUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ManagerErrorException extends ResponseStatusException {
    private final ErrorCodes errorCode;
    private final String technicalMessage;
    private final List<String> errorParameters = new ArrayList<>();

    public ManagerErrorException(HttpStatus httpStatus, ErrorCodes errorCode, String technicalMessage) {
        super(httpStatus, technicalMessage);
        this.errorCode = errorCode;
        this.technicalMessage = technicalMessage;
    }

    public ManagerErrorException(HttpStatus httpStatus, ErrorCodes errorCode, String technicalMessage, Throwable cause) {
        super(httpStatus, technicalMessage, cause);
        this.errorCode = errorCode;
        this.technicalMessage = technicalMessage;
    }

    public ManagerErrorException(HttpStatus httpStatus, ErrorCodes errorCode, String technicalMessage, List<String> errorParameters) {
        super(httpStatus, StringUtils.replacePlaceholders(technicalMessage, errorParameters));
        this.errorCode = errorCode;
        this.technicalMessage = technicalMessage;
        this.errorParameters.addAll(errorParameters);
    }

    public ManagerErrorException(HttpStatus httpStatus, ErrorCodes errorCode, String technicalMessage, String errorParameter1) {
        super(httpStatus, StringUtils.replacePlaceholders(technicalMessage, List.of(errorParameter1)));
        this.errorCode = errorCode;
        this.technicalMessage = technicalMessage;
        this.errorParameters.add(errorParameter1);
    }

    public ManagerErrorException(HttpStatus httpStatus, ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2) {
        super(httpStatus, StringUtils.replacePlaceholders(technicalMessage, List.of(errorParameter1, errorParameter2)));
        this.errorCode = errorCode;
        this.technicalMessage = technicalMessage;
        this.errorParameters.add(errorParameter1);
        this.errorParameters.add(errorParameter2);
    }

    public ManagerErrorException(HttpStatus httpStatus, ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2, String errorParameter3) {
        super(httpStatus, StringUtils.replacePlaceholders(technicalMessage, List.of(errorParameter1, errorParameter2, errorParameter3)));
        this.errorCode = errorCode;
        this.technicalMessage = technicalMessage;
        this.errorParameters.add(errorParameter1);
        this.errorParameters.add(errorParameter2);
        this.errorParameters.add(errorParameter3);
    }
}
