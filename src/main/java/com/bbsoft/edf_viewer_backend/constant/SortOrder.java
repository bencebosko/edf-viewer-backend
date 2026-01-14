package com.bbsoft.edf_viewer_backend.constant;

import com.bbsoft.edf_viewer_backend.exception.EDFViewerException;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum SortOrder {

    ASC("asc"),
    DESC("desc");

    private final String technicalName;

    @JsonValue
    public String getTechnicalName() {
        return technicalName;
    }

    public static SortOrder getByTechnicalName(String technicalName) {
        return Arrays.stream(SortOrder.values()).filter(order -> Objects.equals(order.getTechnicalName(), technicalName))
            .findFirst().orElseThrow(() -> new EDFViewerException("SortOrder cannot be converted from technicalName: " + technicalName));
    }
}
