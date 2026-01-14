package com.bbsoft.edf_viewer_backend.service;

import com.bbsoft.edf_viewer_backend.config.EDFViewerProperties;
import com.bbsoft.edf_viewer_backend.converter.EDFRecordConverter;
import com.bbsoft.edf_viewer_backend.dto.Pageable;
import com.bbsoft.edf_viewer_backend.dto.EDFRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class EDFRecordServiceTest {

    private static final EDFRecord EDF_RECORD_1 = createEdfRecord("1.edf", true, "2026-03-11T12:00:00Z");
    private static final EDFRecord EDF_RECORD_2 = createEdfRecord("2.edf", true, "2026-03-12T12:00:00Z");
    private static final EDFRecord EDF_RECORD_3 = createEdfRecord("3.edf", true, "2026-03-13T12:00:00Z");
    private static final EDFRecord EDF_RECORD_4 = createEdfRecord("4.edf", true, "2026-03-14T12:00:00Z");
    private static final EDFRecord EDF_RECORD_MISSING_DATE = createEdfRecord("missing-date.edf", true, null);
    private static final EDFRecord EDF_RECORD_INVALID = createEdfRecord("invalid.edf", false, "2026-03-10T12:00:00Z");

    private static final List<EDFRecord> EDF_RECORDS = List.of(
        EDF_RECORD_4,
        EDF_RECORD_INVALID,
        EDF_RECORD_1,
        EDF_RECORD_MISSING_DATE,
        EDF_RECORD_2,
        EDF_RECORD_3
    );

    private static final List<EDFRecord> EDF_RECORDS_SORTED_ASC = List.of(
        EDF_RECORD_MISSING_DATE,
        EDF_RECORD_1,
        EDF_RECORD_2,
        EDF_RECORD_3,
        EDF_RECORD_4,
        EDF_RECORD_INVALID
    );

    private static final List<EDFRecord> EDF_RECORDS_SORTED_DESC = List.of(
        EDF_RECORD_4,
        EDF_RECORD_3,
        EDF_RECORD_2,
        EDF_RECORD_1,
        EDF_RECORD_MISSING_DATE,
        EDF_RECORD_INVALID
    );

    @Spy
    private final EDFRecordConverter edfRecordConverter = new EDFRecordConverter();
    @Mock
    private EDFViewerProperties edfViewerProperties;
    @InjectMocks
    private EDFRecordService edfRecordService;

    @BeforeEach
    public void initMocks() {
        edfRecordService.setLoadedEdfFiles(EDF_RECORDS);
    }

    @Test
    public void getListItems_ShouldReturnEmptyPageIfPageNotExist() {
        // GIVEN
        var page = EDF_RECORDS.size();
        var pageSize = 1;
        var sortOrder = "asc";
        var pageable = new Pageable(page, pageSize, sortOrder);
        // WHEN
        var pageOfItems = edfRecordService.getListItems(pageable);
        // THEN
        Assertions.assertTrue(pageOfItems.items().isEmpty());
        Assertions.assertEquals(page, pageOfItems.page());
        Assertions.assertEquals(pageSize, pageOfItems.pageSize());
        Assertions.assertEquals(EDF_RECORDS.size(), pageOfItems.totalCount());
    }

    @Test
    public void getListItems_ShouldReturnTheFirstPageSortedAsc() {
        // GIVEN
        var page = 0;
        var pageSize = EDF_RECORDS.size();
        var sortOrder = "asc";
        var edfPageable = new Pageable(page, pageSize, sortOrder);
        var expectedItems = EDF_RECORDS_SORTED_ASC.stream().map(edfRecordConverter::convertToListItem).toList();
        var expectedItemBeforeLastItem = edfRecordConverter.convertToListItem(EDF_RECORD_4);
        var expectedLastItem = edfRecordConverter.convertToListItem(EDF_RECORD_INVALID);
        // WHEN
        final var pageOfItems = edfRecordService.getListItems(edfPageable);
        // THEN
        Assertions.assertEquals(expectedItems, pageOfItems.items());
        Assertions.assertEquals(expectedItemBeforeLastItem, pageOfItems.items().get(4));
        Assertions.assertEquals(expectedLastItem, pageOfItems.items().getLast());
        Assertions.assertEquals(page, pageOfItems.page());
        Assertions.assertEquals(pageSize, pageOfItems.pageSize());
        Assertions.assertEquals(EDF_RECORDS.size(), pageOfItems.totalCount());
    }

    @Test
    public void getListItems_ShouldReturnTheFirstPageSortedDesc() {
        // GIVEN
        var page = 0;
        var pageSize = EDF_RECORDS.size();
        var sortOrder = "desc";
        var edfPageable = new Pageable(page, pageSize, sortOrder);
        var expectedItems = EDF_RECORDS_SORTED_DESC.stream().map(edfRecordConverter::convertToListItem).toList();
        var expectedItemBeforeLastItem = edfRecordConverter.convertToListItem(EDF_RECORD_MISSING_DATE);
        var expectedLastItem = edfRecordConverter.convertToListItem(EDF_RECORD_INVALID);
        // WHEN
        final var pageOfItems = edfRecordService.getListItems(edfPageable);
        // THEN
        Assertions.assertEquals(expectedItems, pageOfItems.items());
        Assertions.assertEquals(expectedItemBeforeLastItem, pageOfItems.items().get(4));
        Assertions.assertEquals(expectedLastItem, pageOfItems.items().getLast());
        Assertions.assertEquals(page, pageOfItems.page());
        Assertions.assertEquals(pageSize, pageOfItems.pageSize());
        Assertions.assertEquals(EDF_RECORDS.size(), pageOfItems.totalCount());
    }

    @Test
    public void getListItems_ShouldReturnTheFirstElementOfTheLastPage() {
        // GIVEN
        var page = 1;
        var pageSize = EDF_RECORDS.size() - 1;
        var sortOrder = "asc";
        var edfPageable = new Pageable(page, pageSize, sortOrder);
        var expectedTotalCount = 1;
        var expectedLastElement = edfRecordConverter.convertToListItem(EDF_RECORD_INVALID);
        // WHEN
        final var pageOfItems = edfRecordService.getListItems(edfPageable);
        // THEN
        Assertions.assertEquals(expectedTotalCount, pageOfItems.items().size());
        Assertions.assertEquals(expectedLastElement, pageOfItems.items().getFirst());
        Assertions.assertEquals(page, pageOfItems.page());
        Assertions.assertEquals(pageSize, pageOfItems.pageSize());
        Assertions.assertEquals(EDF_RECORDS.size(), pageOfItems.totalCount());
    }

    @Test
    public void getRecord_ShouldFindTheRecordByFileName() {
        // GIVEN
        var recordToFind = EDF_RECORDS.getLast();
        // THEN
        Assertions.assertEquals(recordToFind, edfRecordService.getRecord(recordToFind.getFileName()).orElse(null));
    }

    @Test
    public void getRecord_ShouldNotFindNotExistingRecord() {
        Assertions.assertTrue(edfRecordService.getRecord("notExistingRecord").isEmpty());
    }

    private static EDFRecord createEdfRecord(String fileName, boolean isValid, String recordDateISO) {
        return EDFRecord.builder()
            .fileName(fileName)
            .valid(isValid)
            .recordDate(Objects.nonNull(recordDateISO) ? ZonedDateTime.parse(recordDateISO) : null)
            .build();
    }
}
