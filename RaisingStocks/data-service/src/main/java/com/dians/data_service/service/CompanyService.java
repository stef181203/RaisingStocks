package com.dians.data_service.service;

import com.dians.data_service.domain.Company;
import com.dians.data_service.helper_models.dto.CompanyDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
  /* The explanation of the few confusing names are above
  * the method overrides in the service implementation. */
  Page<CompanyDTO> findAllCompaniesDTOToPage(String sort, int page, int pageSize);
  void save(Company company);
  Optional<Company> findCompanyByCode(String code);
  Optional<Company> findCompanyById(Long id);
  CompanyDTO findByIdToDTO(Long id);
  void deleteById(Long id);
  List<CompanyDTO> findAllCompaniesDTO();
}