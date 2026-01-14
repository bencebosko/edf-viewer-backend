package com.bbsoft.edf_viewer_backend.service;

import com.bbsoft.edf_viewer_backend.config.EDFViewerProperties;
import com.bbsoft.edf_viewer_backend.constant.SortOrder;
import com.bbsoft.edf_viewer_backend.dto.Page;
import com.bbsoft.edf_viewer_backend.exception.EDFViewerException;
import com.bbsoft.edf_viewer_backend.converter.EDFRecordConverter;
import com.bbsoft.edf_viewer_backend.dto.Pageable;
import com.bbsoft.edf_viewer_backend.dto.EDFRecord;
import com.bbsoft.edf_viewer_backend.dto.EDFListItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mipt.edf.EDFParser;
import ru.mipt.edf.EDFParserException;
import ru.mipt.edf.EDFParserResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/* Service for loading and storing EDFRecords in the memory. */
@Service
@RequiredArgsConstructor
@Slf4j
public class EDFRecordService {

    private final EDFViewerProperties zetoProperties;
    private final EDFRecordConverter edfRecordConverter;
    private List<EDFRecord> loadedEdfFiles = Collections.emptyList();

    /* Loads all EDF files from the source directory. Invalid files are also inserted into the loaded files. */
    public void loadEdfFiles() {
        final var sourceDirectory = Paths.get(zetoProperties.getEdfSourceDirectory());
        final var edfFiles = new ArrayList<EDFRecord>();
        try (Stream<Path> filePaths = Files.list(sourceDirectory)) {
            filePaths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    EDFParserResult result = EDFParser.parseEDF(new FileInputStream(path.toFile()));
                    edfFiles.add(edfRecordConverter.convertToRecord(path.getFileName().toString(), true, result));
                } catch (FileNotFoundException ex) {
                    log.info(ex.getMessage());
                } catch (EDFParserException ex) {
                    log.info("EDF file is invalid: {}", path.toAbsolutePath());
                    edfFiles.add(EDFRecord.builder().fileName(path.getFileName().toString()).valid(false).build());
                }
            });
        } catch (IOException ex) {
            throw new EDFViewerException("EDF files cannot be loaded: " + ex.getMessage());
        }
        setLoadedEdfFiles(edfFiles);
    }

    /* Gets a Page of EDFListItem sorted by record date ascending or descending. Invalid files are always put last in the result. */
    public Page<EDFListItem> getListItems(Pageable pageable) {
        List<EDFListItem> pageItems;
        final var page = pageable.getPage();
        final var pageSize = pageable.getPageSize();
        final var totalCount = loadedEdfFiles.size();
        final var startIndex = page * pageSize;
        final var endIndex = Math.min(startIndex + pageSize, totalCount);
        if (startIndex < loadedEdfFiles.size()) {
            pageItems = loadedEdfFiles.stream()
                .map(edfRecordConverter::convertToListItem)
                .sorted(edfListItemComparator(pageable.getSortOrder()))
                .toList()
                .subList(startIndex, endIndex);
        } else {
            pageItems = Collections.emptyList();
        }
        return new Page<>(pageItems, page, pageSize, totalCount);
    }

    /* Gets a full EDFRecord by fileName. If the record not found than an empty Optional is returned. */
    public Optional<EDFRecord> getRecord(String fileName) {
        return loadedEdfFiles.stream().filter(edfRecord -> Objects.equals(edfRecord.getFileName(), fileName)).findAny();
    }

    /* Sets loadedEdfFiles as an UnmodifiableList. */
    void setLoadedEdfFiles(List<EDFRecord> edfFiles) {
        this.loadedEdfFiles = Collections.unmodifiableList(edfFiles);
    }

    /* Comparator for sorting EDFListItem list by recordDate. EDFListItem which are invalid are always put at the end of the list. */
    private Comparator<EDFListItem> edfListItemComparator(SortOrder sortOrder) {
        var validFirstComparator = Comparator.comparing(EDFListItem::isValid).reversed();
        Comparator<ZonedDateTime> nullFirstDateComparator = Comparator.nullsFirst(Comparator.naturalOrder());
        Comparator<EDFListItem> recordDateComparator = (first, second) -> nullFirstDateComparator.compare(first.getRecordDate(), second.getRecordDate());
        return Objects.equals(sortOrder, SortOrder.ASC) ? validFirstComparator.thenComparing(recordDateComparator) : validFirstComparator.thenComparing(recordDateComparator.reversed());
    }
}
