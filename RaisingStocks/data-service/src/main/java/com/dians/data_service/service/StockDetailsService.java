package com.dians.data_service.service;

import com.dians.data_service.domain.Company;
import com.dians.data_service.domain.StockDetailsHistory;
import com.dians.data_service.helper_models.dto.StockDTO;
import com.dians.data_service.helper_models.dto.StockGraphDTO;
import com.dians.data_service.helper_models.microservice.StockValues;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockDetailsService {
  /* The explanation of the few confusing names are above
   * the method overrides in the service implementation. */
  Optional<StockDetailsHistory> findByDateAndCompany(LocalDate date, Company company);
  Page<StockDTO> findAllStocksDTOByCompanyIdToPage(Long companyId, int page, int pageSize, String sort);
  void addStockDetailToCompany(Long companyId, StockDetailsHistory stockDetailsHistory);
  List<StockGraphDTO> findAllStockGraphDTOByCompanyIdAndYear(Long companyId, Integer year);
  List<Integer> findGraphYearsAvailable(Long companyId);
  List<StockValues> findLast30ByCompanyId(Long companyId);
}