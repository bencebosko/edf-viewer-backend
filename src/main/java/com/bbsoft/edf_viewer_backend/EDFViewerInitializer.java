package com.bbsoft.edf_viewer_backend;

import com.bbsoft.edf_viewer_backend.service.EDFRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EDFViewerInitializer implements ApplicationRunner {

    private final EDFRecordService edfRecordService;
    @Value("${server.port}")
    private String port;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        edfRecordService.loadEdfFiles();
        final var applicationStartedText = """
            \n
            ------------------------------------------------------------------
            | EDF viewer has been initialized and running on localhost:port  |
            ------------------------------------------------------------------
            """.replace("port", port);
        log.info(applicationStartedText);
    }
}

