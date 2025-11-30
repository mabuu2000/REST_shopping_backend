package com.shopping.backend.controller;

import com.shopping.backend.dto.ReportDetailResponse;
import com.shopping.backend.dto.ReportListItem;

import com.shopping.backend.service.CeoReportService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ceo/reports")
public class CeoReportController {

    private final CeoReportService reportService;

    @Autowired
    public CeoReportController(CeoReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<?> getReports() {
        String email = AuthUtils.getCurrentUserEmail();
        return ResponseEntity.ok(reportService.listReports(email));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportDetail(@PathVariable UUID reportId) {
        String email = AuthUtils.getCurrentUserEmail();
        return ResponseEntity.ok(reportService.viewReport(reportId, email));
    }
    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestParam(defaultValue = "monthly") String type) {

        String email = AuthUtils.getCurrentUserEmail();

        ReportDetailResponse res = reportService.generateReport(email, type);

        return ResponseEntity.ok(res);
    }

}

