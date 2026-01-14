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
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EDFRecord {

    @EqualsAndHashCode.Include
    private String fileName;
    @Builder.Default
    @JsonProperty("isValid")
    private boolean valid = false;
    @EqualsAndHashCode.Include
    private String id;
    private ZonedDateTime recordDate;
    private String patientName;
    @Builder.Default
    private int numberOfChannels = 0;
    @Builder.Default
    private List<String> channelNames = new ArrayList<>();
    @Builder.Default
    private List<String> channelTypes = new ArrayList<>();
    @Builder.Default
    private int numberOfRecords = 0;
    @Builder.Default
    private int numberOfAnnotations = 0;
}
