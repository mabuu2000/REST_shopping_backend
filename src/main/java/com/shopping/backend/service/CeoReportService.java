package com.shopping.backend.service;

import com.shopping.backend.dto.ProductSaleItem;
import com.shopping.backend.dto.ReportDetailResponse;
import com.shopping.backend.dto.ReportListItem;
import com.shopping.backend.model.Report;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.OrderItemRepository;
import com.shopping.backend.repo.ReportRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CeoReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public CeoReportService(
            ReportRepository reportRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }
    public ReportDetailResponse generateReport(String email, String type) {

        ensureIsCEO(email);

        // 1) Lấy summary từ OrderItemRepository
        List<ProductSaleItem> summary = orderItemRepository.getSalesSummary();

        if (summary.isEmpty()) {
            throw new RuntimeException("No order data available to generate report.");
        }

        // 2) Tạo CSV
        StringBuilder csv = new StringBuilder("Product,Quantity\n");
        for (ProductSaleItem item : summary) {
            csv.append(item.getProductName())
                    .append(",")
                    .append(item.getQuantitySold())
                    .append("\n");
        }

        // 3) Gắn time frame
        String timeFrame = switch (type.toLowerCase()) {
            case "daily" -> "Today";
            case "weekly" -> "This Week";
            case "monthly" -> "This Month";
            default -> "General";
        };

        // 4) Tạo object và lưu vào DB
        Report r = new Report();
        r.setCsvContent(csv.toString());
        r.setPdfContentBase64(null); // Nếu muốn tạo PDF, mình thêm sau
        r.setTimeFrame(timeFrame);
        r.setType(type);
        r.setGeneratedAt(LocalDate.now());

        reportRepository.save(r);

        // 5) Chuẩn bị response
        ReportDetailResponse res = new ReportDetailResponse();
        res.setReportId(r.getId().toString());
        res.setType(type);
        res.setTimeFrame(timeFrame);
        res.setCsvContent(r.getCsvContent());
        res.setPdfBase64(r.getPdfContentBase64());
        res.setItems(summary);

        return res;
    }


    private void ensureIsCEO(String email) {
        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("Not logged in"));

        if (!user.getUsername().equals("ceo_alex")) {
            throw new RuntimeException("Permission denied: CEO only");
        }
    }

    public List<ReportListItem> listReports(String email) {
        ensureIsCEO(email);

        List<Report> reports = reportRepository.findAllByOrderByGeneratedAtDesc();

        if (reports.isEmpty())
            throw new RuntimeException("There aren’t any summaries ready yet.");

        return reports.stream().map(r -> {
            ReportListItem i = new ReportListItem();
            i.setReportId(r.getId().toString());
            i.setType(r.getType());
            i.setTimeFrame(r.getTimeFrame());
            i.setGeneratedAt(r.getGeneratedAt().toString());
            return i;
        }).collect(Collectors.toList());
    }

    public ReportDetailResponse viewReport(UUID reportId, String email) {
        ensureIsCEO(email);

        Report r = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        ReportDetailResponse res = new ReportDetailResponse();
        res.setReportId(r.getId().toString());
        res.setType(r.getType());
        res.setTimeFrame(r.getTimeFrame());
        res.setCsvContent(r.getCsvContent());
        res.setPdfBase64(r.getPdfContentBase64());

        // ✔ LẤY SALES SUMMARY ĐÚNG METHOD
        res.setItems(orderItemRepository.getSalesSummary());

        return res;
    }
}
