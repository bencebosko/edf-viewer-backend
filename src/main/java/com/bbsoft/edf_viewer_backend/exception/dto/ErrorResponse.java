package com.bbsoft.edf_viewer_backend.exception.dto;

import java.time.ZonedDateTime;

public record ErrorResponse(String errorCode, String message, ZonedDateTime occurredAt) {
}
