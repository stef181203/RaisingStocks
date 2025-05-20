package com.dians.data_service.repository;

import com.dians.data_service.domain.Company;
import com.dians.data_service.domain.StockDetailsHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockDetailsRepository extends JpaRepository<StockDetailsHistory, Long> {
  Optional<StockDetailsHistory> findAllByDateAndCompany(LocalDate date, Company company);
  List<StockDetailsHistory> findAllByCompanyId(Long companyId);
  List<StockDetailsHistory> findAllByDateBetweenAndCompanyId(LocalDate startDate, LocalDate endDate, Long companyId);
  Page<StockDetailsHistory> findAllByCompanyId(Long companyId, Pageable pageable);
}