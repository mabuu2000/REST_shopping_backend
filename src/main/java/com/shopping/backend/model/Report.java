package com.shopping.backend.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type; // daily | weekly | monthly

    private String timeFrame; // e.g. "2025-01-01", "2025-W05", "2025-01"

    private LocalDate generatedAt;

    @Column(columnDefinition = "TEXT")
    private String csvContent;

    @Column(columnDefinition = "TEXT")
    private String pdfContentBase64; // hoặc lưu file thật
}

