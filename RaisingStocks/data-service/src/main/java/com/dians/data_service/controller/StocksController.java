package com.dians.data_service.controller;

import com.dians.data_service.helper_models.dto.StockDTO;
import com.dians.data_service.helper_models.dto.StockGraphDTO;
import com.dians.data_service.helper_models.microservice.StockValues;
import com.dians.data_service.service.CompanyService;
import com.dians.data_service.service.StockDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stocks")
public class StocksController {
  private final StockDetailsService stockDetailsService;
  private final CompanyService companyService;

  public StocksController(StockDetailsService stockDetailsService, CompanyService companyService) {
    this.stockDetailsService = stockDetailsService;
    this.companyService = companyService;
  }

  @GetMapping
  @Transactional
  // Returns a page of stocks based on the request parameters.
  public ResponseEntity<Map<String, Object>> getStocks(@RequestParam Long companyId,
                                                       @RequestParam int page,
                                                       @RequestParam int pageSize,
                                                       @RequestParam String sort) {
    Map<String, Object> response = new HashMap<>();
    Page<StockDTO> stocksPage = this.stockDetailsService.findAllStocksDTOByCompanyIdToPage(companyId, page, pageSize, sort);
    response.put("stocks", stocksPage.get().collect(Collectors.toList()));
    response.put("totalPageCount", stocksPage.getTotalPages());
    response.put("companyCode", this.companyService.findByIdToDTO(companyId).getCode());
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/graph")
  // Returns a list of StockGraphDTO based on the request parameters.
  public ResponseEntity<List<StockGraphDTO>> getStocksForGraph(@RequestParam Long companyId,
                                                               @RequestParam Integer year) {
    return ResponseEntity.ok().body(this.stockDetailsService.findAllStockGraphDTOByCompanyIdAndYear(companyId, year));
  }

  @GetMapping("/graph-years")
  // Returns a list of all stock years available for a given company.
  public ResponseEntity<List<Integer>> getYearsForGraph(@RequestParam Long companyId) {
    return ResponseEntity.ok().body(this.stockDetailsService.findGraphYearsAvailable(companyId));
  }

  @GetMapping("/last-30")
  /* Returns a list of the latest 30 stocks for a given
  * company id in descending date order (if available). */
  public ResponseEntity<List<StockValues>> getLast30StocksForCompany(@RequestParam Long companyId) {
    List<StockValues> last30StocksList = this.stockDetailsService.findLast30ByCompanyId(companyId).reversed();

    if(last30StocksList != null) {
      return ResponseEntity.ok().body(last30StocksList);
    }

    return ResponseEntity.notFound().build();
  }

}