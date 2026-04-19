package com.cozisoft.starter.shared.core.errorhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerErrorResponse {
    private String timestamp;
    private int status;
    private String errorCode;
    private List<String> errorParameters;
    private String message;
    private String path;
    private String trace;
}
