package com.bbsoft.edf_viewer_backend.exception.handler;

import com.bbsoft.edf_viewer_backend.constant.ErrorCode;
import com.bbsoft.edf_viewer_backend.exception.EDFViewerException;
import com.bbsoft.edf_viewer_backend.exception.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
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

/* Global Exception handler for handling exceptions thrown in the application.
Exceptions are converted to ErrorResponse with the appropriate HttpStatus. */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        log.info(getStackTraceAsString(ex));
        return (ResponseEntity) createResponseEntity(ErrorCode.VALIDATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler({EDFViewerException.class})
    protected ResponseEntity<ErrorResponse> handleZetoException(EDFViewerException ex) {
        log.info(getStackTraceAsString(ex));
        return createResponseEntity(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleOtherException(Exception ex) {
        log.error(getStackTraceAsString(ex));
        return createResponseEntity(ErrorCode.GENERAL_ERROR, ex.getMessage());
    }

    private String getStackTraceAsString(Exception ex) {
        final var stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode.getErrorCode(), message));
    }
}
