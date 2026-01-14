package com.bbsoft.edf_viewer_backend.controller;

import com.bbsoft.edf_viewer_backend.dto.EDFListItem;
import com.bbsoft.edf_viewer_backend.dto.Pageable;
import com.bbsoft.edf_viewer_backend.dto.EDFRecord;
import com.bbsoft.edf_viewer_backend.dto.Page;
import com.bbsoft.edf_viewer_backend.service.EDFRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/edf")
@RequiredArgsConstructor
@Slf4j
public class EDFRecordController {

    private final EDFRecordService edfRecordService;

    /* Endpoint for getting a Page of EDFListItem. Pagination params are mandatory. */
    @GetMapping
    public ResponseEntity<Page<EDFListItem>> getListItems(@Validated Pageable pageable) {
        log.info("Get EDF list items with pageable: {}", pageable);
        return ResponseEntity.ok(edfRecordService.getListItems(pageable));
    }

    /* Endpoint for getting an EDFRecord containing all data of an EDF file. */
    @GetMapping("/{fileName:.+\\.edf}")
    public ResponseEntity<EDFRecord> getRecord(@PathVariable String fileName) {
        log.info("Get EDF record by fileName {}", fileName);
        return ResponseEntity.of(edfRecordService.getRecord(fileName));
    }
}
