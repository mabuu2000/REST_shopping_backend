package com.shopping.backend.repo;

import com.shopping.backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findAllByOrderByGeneratedAtDesc();
}
