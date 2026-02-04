package com.bbsoft.edf_viewer_backend.exception.handler;

import com.bbsoft.edf_viewer_backend.config.ClockProvider;
import com.bbsoft.edf_viewer_backend.constant.ErrorCodes;
import com.bbsoft.edf_viewer_backend.exception.EDFViewerException;
import com.bbsoft.edf_viewer_backend.exception.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ClockProvider clockProvider;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        log.info(getStackTraceAsString(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(ErrorCodes.INVALID_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler({EDFViewerException.class})
    protected ResponseEntity<ErrorResponse> handleEDFViewerException(EDFViewerException ex) {
        log.info(getStackTraceAsString(ex));
        return ResponseEntity.status(ex.getHttpStatus()).body(createErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(getStackTraceAsString(ex));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(ErrorCodes.EDF_VIEWER_ERROR, ex.getMessage()));
    }

    private String getStackTraceAsString(Exception ex) {
        var stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private ErrorResponse createErrorResponse(String errorCode, String message) {
        return new ErrorResponse(errorCode, message, ZonedDateTime.now(clockProvider.getClock()));
    }
}
