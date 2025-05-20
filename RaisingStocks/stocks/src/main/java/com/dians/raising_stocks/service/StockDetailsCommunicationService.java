package com.dians.raising_stocks.service;

import com.dians.raising_stocks.domain.TechnicalIndicator;
import com.dians.raising_stocks.helper_models.dto.StockGraphDTO;

import java.util.List;
import java.util.Map;

public interface StockDetailsCommunicationService {
  /* The explanation of the few confusing names are above
  * the method overrides in the service implementation. */
  Map<String, Object> fetchAllStocksDTOByCompanyIdToPage(Long companyId, int page, int pageSize, String sort);
  List<StockGraphDTO> fetchAllStockGraphDTOByCompanyIdAndYear(Long companyId, Integer year);
  List<Integer> fetchGraphYearsAvailable(Long companyId);
  List<TechnicalIndicator> fetchTrendIndicatorsByCompanyId(Long companyId);
  List<TechnicalIndicator> fetchMomentumIndicatorsByCompanyId(Long companyId);
  List<String> fetchSignalsByCompanyId(Long companyId);
}