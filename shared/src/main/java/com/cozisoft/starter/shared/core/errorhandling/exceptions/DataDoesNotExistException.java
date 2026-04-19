package com.cozisoft.starter.shared.core.errorhandling.exceptions;

import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.ManagerErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class DataDoesNotExistException extends ManagerErrorException {

    public DataDoesNotExistException(ErrorCodes errorCode, String technicalMessage) {
        super(HttpStatus.NOT_FOUND, errorCode, technicalMessage);
    }

    public DataDoesNotExistException(ErrorCodes errorCode, String technicalMessage, List<String> errorParameters) {
        super(HttpStatus.NOT_FOUND, errorCode, technicalMessage, errorParameters);
    }

    public DataDoesNotExistException(ErrorCodes errorCode, String technicalMessage, String errorParameter1) {
        super(HttpStatus.NOT_FOUND, errorCode, technicalMessage, errorParameter1);
    }

    public DataDoesNotExistException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2) {
        super(HttpStatus.NOT_FOUND, errorCode, technicalMessage, errorParameter1, errorParameter2);
    }

    public DataDoesNotExistException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2, String errorParameter3) {
        super(HttpStatus.NOT_FOUND, errorCode, technicalMessage, errorParameter1, errorParameter2, errorParameter3);
    }
}
