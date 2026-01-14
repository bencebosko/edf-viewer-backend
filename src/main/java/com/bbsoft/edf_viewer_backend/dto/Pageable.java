package com.bbsoft.edf_viewer_backend.dto;

import com.bbsoft.edf_viewer_backend.constant.SortOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Pageable {

    @Min(0)
    private final int page;
    @Min(1)
    private final int pageSize;
    @NotNull
    private final SortOrder sortOrder;

    public Pageable(int page, int pageSize, String sortOrder) {
        this.page = page;
        this.pageSize = pageSize;
        this.sortOrder = SortOrder.getByTechnicalName(sortOrder);
    }
}
