package com.dians.raising_stocks.service;

import com.dians.raising_stocks.helper_models.dto.CompanyDTO;

import java.util.List;
import java.util.Map;

public interface CompanyCommunicationService {
  /* The explanation of the few confusing names are above
  * the method overrides in the service implementation. */
  Map<String, Object> fetchAllCompaniesDTOToPage(int page, int pageSize, String sort);
  List<CompanyDTO> fetchAllCompaniesDTO();
}