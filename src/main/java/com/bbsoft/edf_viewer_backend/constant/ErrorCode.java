package com.bbsoft.edf_viewer_backend.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/* Error codes used in the application mapped to http status code. */
@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "general-error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "validation-error");

    private final HttpStatus httpStatus;
    private final String errorCode;
}
