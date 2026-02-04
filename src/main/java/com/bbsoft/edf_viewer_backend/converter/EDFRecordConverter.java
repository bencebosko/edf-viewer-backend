package com.bbsoft.edf_viewer_backend.converter;

import com.bbsoft.edf_viewer_backend.dto.EDFRecord;
import com.bbsoft.edf_viewer_backend.dto.EDFListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mipt.edf.EDFParserResult;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Component
@Slf4j
public class EDFRecordConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final String SUBJECT_SEPARATOR = " ";

    public EDFRecord convertToRecord(String fileName, boolean isValid, EDFParserResult edfParserResult) {
        final var edfHeader = edfParserResult.getHeader();
        final var subjectTokens = Objects.nonNull(edfHeader.getSubjectID()) ? edfHeader.getSubjectID().split(SUBJECT_SEPARATOR) : new String[0];
        return EDFRecord.builder()
            .fileName(fileName)
            .valid(isValid)
            .id(subjectTokens.length != 0 ? subjectTokens[0] : null)
            .recordDate(parseEdfDate(edfHeader.getStartDate(), fileName))
            .patientName(subjectTokens.length == 4 ? subjectTokens[3] : null)
            .numberOfChannels(edfHeader.getNumberOfChannels())
            .channelNames(Objects.nonNull(edfHeader.getChannelLabels()) ? Arrays.asList(edfHeader.getChannelLabels()) : new ArrayList<>())
            .channelTypes(Objects.nonNull(edfHeader.getTransducerTypes()) ? Arrays.asList(edfHeader.getTransducerTypes()) : new ArrayList<>())
            .numberOfRecords(edfHeader.getNumberOfRecords())
            .numberOfAnnotations(Objects.nonNull(edfParserResult.getAnnotations()) ? edfParserResult.getAnnotations().size() : 0)
            .build();
    }

    public EDFListItem convertToListItem(EDFRecord edfRecord) {
        return EDFListItem.builder()
            .fileName(edfRecord.getFileName())
            .valid(edfRecord.isValid())
            .id(edfRecord.getId())
            .recordDate(edfRecord.getRecordDate())
            .build();
    }

    private ZonedDateTime parseEdfDate(String recordDate, String fileName) {
        if (Objects.isNull(recordDate)) {
            return null;
        }
        try {
            return LocalDate.parse(recordDate, DATE_TIME_FORMATTER).atStartOfDay(ZoneOffset.UTC);
        } catch (DateTimeParseException ex) {
            log.info("Failed to parse record date for EDF file: {}", fileName);
            return null;
        }
    }
}
