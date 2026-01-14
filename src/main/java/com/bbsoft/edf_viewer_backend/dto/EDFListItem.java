package com.bbsoft.edf_viewer_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EDFListItem {

    @EqualsAndHashCode.Include
    private String fileName;
    @Builder.Default
    @JsonProperty("isValid")
    private boolean valid = false;
    private String id;
    private ZonedDateTime recordDate;
}
