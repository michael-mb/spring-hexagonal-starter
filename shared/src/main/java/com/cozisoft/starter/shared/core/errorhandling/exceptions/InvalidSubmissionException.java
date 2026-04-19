package com.cozisoft.starter.shared.core.errorhandling.exceptions;

import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.ManagerErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidSubmissionException extends ManagerErrorException {

    public InvalidSubmissionException(ErrorCodes errorCode, String technicalMessage) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage);
    }

    public InvalidSubmissionException(ErrorCodes errorCode, String technicalMessage, List<String> errorParameters) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameters);
    }

    public InvalidSubmissionException(ErrorCodes errorCode, String technicalMessage, String errorParameter1) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameter1);
    }

    public InvalidSubmissionException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameter1, errorParameter2);
    }

    public InvalidSubmissionException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2, String errorParameter3) {
        super(HttpStatus.BAD_REQUEST, errorCode, technicalMessage, errorParameter1, errorParameter2, errorParameter3);
    }
}
