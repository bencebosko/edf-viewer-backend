package com.bbsoft.edf_viewer_backend.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "edf-viewer")
@RequiredArgsConstructor
@Getter
public class EDFViewerProperties {

    /* Source directory of the EDF files. */
    @NotNull
    private final String edfSourceDirectory;
}
