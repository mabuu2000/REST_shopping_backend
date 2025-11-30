package com.shopping.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReportDetailResponse {
    private String reportId;
    private String type;
    private String timeFrame;

    // list of product summary
    private List<ProductSaleItem> items;

    // export support
    private String csvContent;
    private String pdfBase64;
}
