package com.cozisoft.starter.shared.core.errorhandling.exceptions;

import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.ManagerErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class DataAlreadyExistsException extends ManagerErrorException {

    public DataAlreadyExistsException(ErrorCodes errorCode, String technicalMessage) {
        super(HttpStatus.CONFLICT, errorCode, technicalMessage);
    }

    public DataAlreadyExistsException(ErrorCodes errorCode, String technicalMessage, List<String> errorParameters) {
        super(HttpStatus.CONFLICT, errorCode, technicalMessage, errorParameters);
    }

    public DataAlreadyExistsException(ErrorCodes errorCode, String technicalMessage, String errorParameter1) {
        super(HttpStatus.CONFLICT, errorCode, technicalMessage, errorParameter1);
    }

    public DataAlreadyExistsException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2) {
        super(HttpStatus.CONFLICT, errorCode, technicalMessage, errorParameter1, errorParameter2);
    }

    public DataAlreadyExistsException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2, String errorParameter3) {
        super(HttpStatus.CONFLICT, errorCode, technicalMessage, errorParameter1, errorParameter2, errorParameter3);
    }
}
