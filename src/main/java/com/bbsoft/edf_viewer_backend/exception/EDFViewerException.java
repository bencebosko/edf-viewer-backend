package com.bbsoft.edf_viewer_backend.exception;

import com.bbsoft.edf_viewer_backend.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EDFViewerException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;

    public EDFViewerException(String message) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.EDF_VIEWER_ERROR, message);
    }

    protected EDFViewerException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
