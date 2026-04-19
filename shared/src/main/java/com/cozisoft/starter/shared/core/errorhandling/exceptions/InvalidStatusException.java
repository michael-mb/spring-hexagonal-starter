package com.cozisoft.starter.shared.core.errorhandling.exceptions;

import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.ManagerErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidStatusException extends ManagerErrorException {

    public InvalidStatusException(ErrorCodes errorCode, String technicalMessage) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage);
    }

    public InvalidStatusException(ErrorCodes errorCode, String technicalMessage, List<String> errorParameters) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameters);
    }

    public InvalidStatusException(ErrorCodes errorCode, String technicalMessage, String errorParameter1) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameter1);
    }

    public InvalidStatusException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameter1, errorParameter2);
    }

    public InvalidStatusException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2, String errorParameter3) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameter1, errorParameter2, errorParameter3);
    }
}
