package com.shopping.backend.service;

import com.shopping.backend.dto.SalesItemDto;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.OrderRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Generate the Data
    public List<SalesItemDto> generateReport(String username, String period) {
        // Security: Check if CEO
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ceo".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Access Denied: Only CEO can view reports.");
        }

        LocalDateTime startDate;
        switch (period.toLowerCase()) {
            case "daily":
                startDate = LocalDateTime.now().minusDays(1);
                break;
            case "weekly":
                startDate = LocalDateTime.now().minusWeeks(1);
                break;
            case "monthly":
                startDate = LocalDateTime.now().minusMonths(1);
                break;
            default:
                throw new RuntimeException("Invalid period. Use 'daily', 'weekly', or 'monthly'.");
        }

        List<SalesItemDto> stats = orderRepository.getSalesStats(startDate);

        // Alt Sequence 2a: No reports found
        if (stats.isEmpty()) {
            throw new RuntimeException("There aren't any summaries ready yet.");
        }

        return stats;
    }

    // 2. Convert to CSV Format (Step 5)
    public String convertToCsv(List<SalesItemDto> stats) {
        StringBuilder csv = new StringBuilder();
        csv.append("Product Name,Total Sold,Total Revenue\n"); // Header

        for (SalesItemDto item : stats) {
            csv.append(item.getProductName()).append(",")
                    .append(item.getTotalQuantitySold()).append(",")
                    .append(item.getTotalRevenue()).append("\n");
        }
        return csv.toString();
    }
}
