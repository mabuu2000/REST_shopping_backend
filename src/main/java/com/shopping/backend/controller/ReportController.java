package com.shopping.backend.controller;

import com.shopping.backend.dto.SalesItemDto;
import com.shopping.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // UC22: View Report (JSON)
    // Usage: GET /reports/summary?period=weekly
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestParam String period) {
        try {
            String username = getCurrentUsername();
            List<SalesItemDto> report = reportService.generateReport(username, period);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            // Handles "Access Denied" or "No reports found"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // UC22 Step 5: Export as CSV
    // Usage: GET /reports/export?period=weekly
    @GetMapping("/export")
    public ResponseEntity<?> exportReport(@RequestParam String period) {
        try {
            String username = getCurrentUsername();
            List<SalesItemDto> report = reportService.generateReport(username, period);
            String csvData = reportService.convertToCsv(report);

            // Return as a downloadable file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales_report.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvData);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
