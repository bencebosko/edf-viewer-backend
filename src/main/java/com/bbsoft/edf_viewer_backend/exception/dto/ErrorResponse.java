package com.bbsoft.edf_viewer_backend.exception.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String errorCode;
    private final ZonedDateTime occurredAt;
    private final String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.occurredAt = LocalDateTime.now().atZone(ZoneOffset.UTC);
        this.message = message;
    }
}
