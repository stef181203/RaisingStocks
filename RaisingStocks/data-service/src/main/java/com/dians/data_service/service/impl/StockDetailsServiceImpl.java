package com.dians.data_service.service.impl;

import com.dians.data_service.domain.Company;
import com.dians.data_service.domain.StockDetailsHistory;
import com.dians.data_service.helper_models.dto.StockDTO;
import com.dians.data_service.helper_models.dto.StockGraphDTO;
import com.dians.data_service.helper_models.microservice.StockValues;
import com.dians.data_service.mapper.DTOMapper;
import com.dians.data_service.repository.CompanyRepository;
import com.dians.data_service.repository.StockDetailsRepository;
import com.dians.data_service.service.StockDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class StockDetailsServiceImpl implements StockDetailsService {
  private final StockDetailsRepository stockDetailsRepository;
  private final CompanyRepository companyRepository;
  private final DTOMapper dtoMapper;

  public StockDetailsServiceImpl(StockDetailsRepository stockDetailsRepository, CompanyRepository companyRepository, DTOMapper dtoMapper) {
    this.stockDetailsRepository = stockDetailsRepository;
    this.companyRepository = companyRepository;
    this.dtoMapper = dtoMapper;
  }

  @Override
  public Optional<StockDetailsHistory> findByDateAndCompany(LocalDate date, Company company) {
    return this.stockDetailsRepository.findAllByDateAndCompany(date, company);
  }

  @Override
  @Transactional
  /* This method is used by the WriteFilter from the Web Scraper
   * in order to add a scraped stocks to a certain company. */
  public void addStockDetailToCompany(Long companyId, StockDetailsHistory stockDetailsHistory) {
    Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found!"));
    stockDetailsHistory.setCompany(company);
    company.getStockHistory().add(stockDetailsHistory);
    company.updateStockInfo(stockDetailsHistory.getDateAsString());
    this.stockDetailsRepository.save(stockDetailsHistory);
  }

  @Override
  /* Finds all stocks by company id and year, maps them to DTO
   * for the graph in the front-end and returns them as a list. */
  public List<StockGraphDTO> findAllStockGraphDTOByCompanyIdAndYear(Long companyId, Integer year) {
    LocalDate startDate = LocalDate.of(year, 1, 1);
    LocalDate endDate = LocalDate.of(year, 12, 31);
    return this.stockDetailsRepository.findAllByDateBetweenAndCompanyId(startDate, endDate, companyId)
            .stream()
            .map(dtoMapper::convertToStockGraphDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional
  /* Finds the years for a certain company for which the company
   * has stock details in the database and returns them as a list. */
  public List<Integer> findGraphYearsAvailable(Long companyId) {
    Set<Integer> yearSet = new TreeSet<>();
    this.stockDetailsRepository.findAllByCompanyId(companyId)
            .forEach(stock -> {
              yearSet.add(Integer.parseInt(stock.getDateAsString().split("\\.")[2]));
            });
    return yearSet.stream().toList().reversed();
  }

  @Override
  /* Finds the latest 30 stock details by company id, maps them to StockValues and returns them as a list. */
  public List<StockValues> findLast30ByCompanyId(Long companyId) {
    Pageable pageable = PageRequest.of(0, 30, Sort.by("date").descending());
    return this.stockDetailsRepository.findAllByCompanyId(companyId, pageable).stream().map(dtoMapper::convertToStockValues).toList();
  }

  @Override
  @Transactional
  /* Gets a page from the repository containing stocks details found by
   * company id, maps them to DTO, sorts them and returns the new page. */
  public Page<StockDTO> findAllStocksDTOByCompanyIdToPage(Long companyId, int page, int pageSize, String sort) {
    Pageable pageable = CompanyServiceImpl.getPageableObject(sort, page, pageSize);

    Page<StockDetailsHistory> stocksPage = this.stockDetailsRepository.findAllByCompanyId(companyId, pageable);
    List<StockDTO> stocksList = stocksPage
            .stream()
            .map(dtoMapper::convertToStockDTO).collect(Collectors.toList());

    return new PageImpl<>(stocksList, stocksPage.getPageable(), stocksPage.getTotalElements());
  }

}