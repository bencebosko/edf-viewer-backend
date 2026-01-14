package com.bbsoft.edf_viewer_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Page<T>(List<T> items, int page, int pageSize, long totalCount) {
}
