package com.bbsoft.edf_viewer_backend.exception;

import com.bbsoft.edf_viewer_backend.constant.ErrorCode;
import lombok.Getter;

@Getter
public class EDFViewerException extends RuntimeException {

    private final ErrorCode errorCode;

    public EDFViewerException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EDFViewerException(String message) {
        this(ErrorCode.GENERAL_ERROR, message);
    }
}
