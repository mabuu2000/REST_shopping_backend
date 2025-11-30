package com.shopping.backend.dto;

import lombok.Data;

@Data
public class ReportListItem {
    private String reportId;
    private String type;
    private String timeFrame;
    private String generatedAt;
}
