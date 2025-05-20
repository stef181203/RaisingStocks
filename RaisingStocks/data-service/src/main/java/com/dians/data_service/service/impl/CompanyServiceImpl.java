package com.dians.data_service.service.impl;

import com.dians.data_service.domain.Company;
import com.dians.data_service.helper_models.dto.CompanyDTO;
import com.dians.data_service.mapper.DTOMapper;
import com.dians.data_service.repository.CompanyRepository;
import com.dians.data_service.service.CompanyService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository companyRepository;
  private final DTOMapper dtoMapper;

  public CompanyServiceImpl(CompanyRepository companyRepository, DTOMapper dtoMapper) {
    this.companyRepository = companyRepository;
    this.dtoMapper = dtoMapper;
  }

  /* We are using this method to create a Pageable object
   * with the given sort method, page number and page size.
   * The method is static because it is used in StockDetailsServiceImpl
   * in order to cut the duplicate code we had in these two services. */
  static Pageable getPageableObject(String sort, int page, int pageSize) {
    String sortBy = sort.split("-")[0];
    String order = sort.split("-")[1];

    Pageable pageable;
    if(order.equals("asc")) {
      pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).ascending());
    }
    else {
      pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
    }

    return pageable;
  }

  @Override
  /* Gets a page of all the companies from the repository,
  maps them to DTO, sorts them and returns the new page. */
  public Page<CompanyDTO> findAllCompaniesDTOToPage(String sort, int page, int pageSize) {
    Pageable pageable = getPageableObject(sort, page, pageSize);

    Page<Company> companiesPage = this.companyRepository.findAll(pageable);
    List<CompanyDTO> companiesList = companiesPage
            .stream()
            .map(dtoMapper::convertToCompanyDTO).collect(Collectors.toList());

    return new PageImpl<>(companiesList, companiesPage.getPageable(), companiesPage.getTotalElements());
  }

  @Override
  public void save(Company company) {
    this.companyRepository.save(company);
  }

  @Override
  public Optional<Company> findCompanyByCode(String code) {
    return this.companyRepository.findCompanyByCode(code);
  }

  @Override
  public Optional<Company> findCompanyById(Long id) {
    return this.companyRepository.findById(id);
  }

  @Override
  /* Finds company using the id, maps it to DTO and returns it. */
  public CompanyDTO findByIdToDTO(Long id) {
    return this.companyRepository.findById(id)
            .map(dtoMapper::convertToCompanyDTO)
            .orElseThrow(RuntimeException::new);
  }

  @Override
  public void deleteById(Long id) {
    this.companyRepository.deleteById(id);
  }

  @Override
  /* Finds all companies from the database,
  * maps them to DTO and returns a list of them all. */
  public List<CompanyDTO> findAllCompaniesDTO() {
    return this.companyRepository.findAll()
            .stream()
            .map(dtoMapper::convertToCompanyDTO).collect(Collectors.toList());
  }

}