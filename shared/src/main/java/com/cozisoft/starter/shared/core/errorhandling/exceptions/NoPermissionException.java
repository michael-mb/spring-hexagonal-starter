package com.cozisoft.starter.shared.core.errorhandling.exceptions;

import com.cozisoft.starter.shared.core.errorhandling.ErrorCodes;
import com.cozisoft.starter.shared.core.errorhandling.ManagerErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class NoPermissionException extends ManagerErrorException {

    public NoPermissionException(ErrorCodes errorCode, String technicalMessage) {
        super(HttpStatus.FORBIDDEN, errorCode, technicalMessage);
    }

    public NoPermissionException(ErrorCodes errorCode, String technicalMessage, List<String> errorParameters) {
        super(HttpStatus.FORBIDDEN, errorCode, technicalMessage, errorParameters);
    }

    public NoPermissionException(ErrorCodes errorCode, String technicalMessage, String errorParameter1) {
        super(HttpStatus.FORBIDDEN, errorCode, technicalMessage, errorParameter1);
    }

    public NoPermissionException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2) {
        super(HttpStatus.FORBIDDEN, errorCode, technicalMessage, errorParameter1, errorParameter2);
    }

    public NoPermissionException(ErrorCodes errorCode, String technicalMessage, String errorParameter1, String errorParameter2, String errorParameter3) {
        super(HttpStatus.FORBIDDEN, errorCode, technicalMessage, errorParameter1, errorParameter2, errorParameter3);
    }
}
