package com.bbsoft.edf_viewer_backend.converter;

import com.bbsoft.edf_viewer_backend.dto.EDFRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mipt.edf.EDFAnnotation;
import ru.mipt.edf.EDFHeader;
import ru.mipt.edf.EDFParserResult;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EDFRecordConverterTest {

    private static final String FILENAME = "first.edf";
    private static final boolean IS_VALID = true;
    private static final String ID = "EDF001";
    private static final String RECORD_DATE = "01.12.25";
    private static final ZonedDateTime EXPECTED_RECORD_DATE = ZonedDateTime.of(2025, 12, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    private final EDFRecordConverter edfRecordConverter = new EDFRecordConverter();

    @Mock
    private EDFParserResult edfParserResult;
    @Mock
    private EDFHeader edfHeader;
    @Mock
    private EDFAnnotation edfAnnotation;

    @BeforeEach
    public void initMocks() {
        Mockito.lenient().when(edfHeader.getSubjectID()).thenReturn(null);
        Mockito.lenient().when(edfHeader.getStartDate()).thenReturn(null);
        Mockito.lenient().when(edfHeader.getNumberOfChannels()).thenReturn(0);
        Mockito.lenient().when(edfHeader.getChannelLabels()).thenReturn(null);
        Mockito.lenient().when(edfHeader.getTransducerTypes()).thenReturn(null);
        Mockito.lenient().when(edfHeader.getNumberOfRecords()).thenReturn(0);
        Mockito.lenient().when(edfParserResult.getHeader()).thenReturn(edfHeader);
        Mockito.lenient().when(edfParserResult.getAnnotations()).thenReturn(null);
    }

    @Test
    public void convertToRecord_ShouldMapMissingFieldsToNull() {
        // WHEN
        final var edfRecord = edfRecordConverter.convertToRecord(null, false, edfParserResult);
        // THEN
        Assertions.assertNull(edfRecord.getFileName());
        Assertions.assertFalse(edfRecord.isValid());
        Assertions.assertNull(edfRecord.getId());
        Assertions.assertNull(edfRecord.getRecordDate());
        Assertions.assertEquals(0, edfRecord.getNumberOfChannels());
        Assertions.assertTrue(edfRecord.getChannelNames().isEmpty());
        Assertions.assertTrue(edfRecord.getChannelTypes().isEmpty());
        Assertions.assertEquals(0, edfRecord.getNumberOfRecords());
        Assertions.assertEquals(0, edfRecord.getNumberOfAnnotations());
    }

    @Test
    public void convertToRecord_ShouldMapInvalidStartDateToNull() {
        // GIVEN
        var startDate = "invalid";
        Mockito.when(edfHeader.getStartDate()).thenReturn(startDate);
        // WHEN
        var edfRecord = edfRecordConverter.convertToRecord(null, false, edfParserResult);
        // THEN
        Assertions.assertNull(edfRecord.getRecordDate());
    }

    @Test
    public void convertToRecord_ShouldMapInvalidSubjectIDToNullPatientName() {
        // GIVEN
        var subjectID = "";
        Mockito.when(edfHeader.getSubjectID()).thenReturn(subjectID);
        // WHEN
        var edfRecord = edfRecordConverter.convertToRecord(null, false, edfParserResult);
        // THEN
        Assertions.assertNull(edfRecord.getPatientName());
    }

    @Test
    public void convertToRecord_ShouldMapEveryFieldsCorrectly() {
        // GIVEN
        var patientName = "John_Doe";
        var numberOfChannels = 2;
        var channelNames = new String[]{"Label1", "Label2"};
        var transducerTypes = new String[]{"Type1", "Type2"};
        var numberOfRecords = 1;
        Mockito.when(edfHeader.getSubjectID()).thenReturn(ID + " X X " + patientName);
        Mockito.when(edfHeader.getStartDate()).thenReturn(RECORD_DATE);
        Mockito.when(edfHeader.getNumberOfChannels()).thenReturn(numberOfChannels);
        Mockito.when(edfHeader.getChannelLabels()).thenReturn(channelNames);
        Mockito.when(edfHeader.getTransducerTypes()).thenReturn(transducerTypes);
        Mockito.when(edfHeader.getNumberOfRecords()).thenReturn(numberOfRecords);
        Mockito.when(edfParserResult.getAnnotations()).thenReturn(List.of(edfAnnotation));
        // WHEN
        final var edfRecord = edfRecordConverter.convertToRecord(FILENAME, true, edfParserResult);
        // THEN
        Assertions.assertEquals(FILENAME, edfRecord.getFileName());
        Assertions.assertTrue(edfRecord.isValid());
        Assertions.assertEquals(ID, edfRecord.getId());
        Assertions.assertEquals(EXPECTED_RECORD_DATE, edfRecord.getRecordDate());
        Assertions.assertEquals(numberOfChannels, edfRecord.getNumberOfChannels());
        Assertions.assertEquals(List.of(channelNames), edfRecord.getChannelNames());
        Assertions.assertEquals(List.of(transducerTypes), edfRecord.getChannelTypes());
        Assertions.assertEquals(numberOfRecords, edfRecord.getNumberOfRecords());
        Assertions.assertEquals(1, edfRecord.getNumberOfAnnotations());
    }

    @Test
    public void convertToListItem_ShouldMapEDFRecordToEDFListItem() {
        // GIVEN
        var edfRecord = createEdfRecord();
        // WHEN
        var edfRecordListItem = edfRecordConverter.convertToListItem(edfRecord);
        // THEN
        Assertions.assertEquals(FILENAME, edfRecordListItem.getFileName());
        Assertions.assertEquals(IS_VALID, edfRecordListItem.isValid());
        Assertions.assertEquals(ID, edfRecordListItem.getId());
        Assertions.assertEquals(EXPECTED_RECORD_DATE, edfRecordListItem.getRecordDate());
    }

    private EDFRecord createEdfRecord() {
        return EDFRecord.builder()
            .fileName(FILENAME)
            .valid(IS_VALID)
            .id(ID)
            .recordDate(EXPECTED_RECORD_DATE)
            .build();
    }
}
