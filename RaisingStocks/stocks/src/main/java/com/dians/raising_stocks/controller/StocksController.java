package com.dians.raising_stocks.controller;

import com.dians.raising_stocks.domain.TechnicalIndicator;
import com.dians.raising_stocks.helper_models.dto.StockGraphDTO;
import com.dians.raising_stocks.service.StockDetailsCommunicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StocksController {
  private final StockDetailsCommunicationService stockDetailsCommunicationService;

  public StocksController(StockDetailsCommunicationService stockDetailsCommunicationService, WebClient.Builder webClientBuilder) {
    this.stockDetailsCommunicationService = stockDetailsCommunicationService;
  }

  @GetMapping
  /* Fetches a page of StockDTO objects based on the request parameters through
  * the stocks communication service directly from data-service (microservice). */
  public ResponseEntity<Map<String, Object>> getStocks(@RequestParam Long companyId,
                                                       @RequestParam int page,
                                                       @RequestParam int pageSize,
                                                       @RequestParam String sort) {
    Map<String, Object> response = this.stockDetailsCommunicationService.fetchAllStocksDTOByCompanyIdToPage(companyId, page, pageSize, sort);

    if(response != null) {
      return ResponseEntity.ok().body(response);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/graph")
  /* Fetches a page of StockGraphDTO objects based on the request parameters through
   * the stocks communication service directly from data-service (microservice). */
  public ResponseEntity<List<StockGraphDTO>> getStocksForGraph(@RequestParam Long companyId,
                                                               @RequestParam Integer year) {
    List<StockGraphDTO> stocksGraphDTOList = this.stockDetailsCommunicationService.fetchAllStockGraphDTOByCompanyIdAndYear(companyId, year);

    if(stocksGraphDTOList != null) {
      return ResponseEntity.ok().body(stocksGraphDTOList);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/graph-years")
  /* Fetches a List of available years for a specific stock based on the
  * company id. Communicates and gets the data through the stock details
  * communication service directly from data-service (microservice). */
  public ResponseEntity<List<Integer>> getYearsForGraph(@RequestParam Long companyId) {
    List<Integer> graphYearsList = this.stockDetailsCommunicationService.fetchGraphYearsAvailable(companyId);

    if(graphYearsList != null) {
      return ResponseEntity.ok().body(graphYearsList);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/trend-indicators")
  /* Fetches a List of TechnicalIndicator (trend indicators) objects for a specific stock
  * based on the company id. Communicates and gets the data through the stock details
  * communication service directly from technical-indicator-service (microservice). */
  public ResponseEntity<List<TechnicalIndicator>> getTrendIndicators(@RequestParam Long companyId) {
    List<TechnicalIndicator> trendIndicatorsList = this.stockDetailsCommunicationService.fetchTrendIndicatorsByCompanyId(companyId);

    if(trendIndicatorsList != null) {
      return ResponseEntity.ok().body(trendIndicatorsList);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/momentum-indicators")
  /* Fetches a List of TechnicalIndicator (momentum indicators) objects for a specific stock
   * based on the company id. Communicates and gets the data through the stock details
   * communication service directly from technical-indicator-service (microservice). */
  public ResponseEntity<List<TechnicalIndicator>> getMomentumIndicators(@RequestParam Long companyId) {
    List<TechnicalIndicator> momentumIndicatorsList = this.stockDetailsCommunicationService.fetchMomentumIndicatorsByCompanyId(companyId);

    if(momentumIndicatorsList != null) {
      return ResponseEntity.ok().body(momentumIndicatorsList);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/signals")
  /* Fetches a List of signals for a specific stock based on the
  * company id. Communicates and gets the data through the stock details
  * communication service directly from technical-indicator-service (microservice). */
  public ResponseEntity<List<String>> getSignals(@RequestParam Long companyId) {
    List<String> signalsList = this.stockDetailsCommunicationService.fetchSignalsByCompanyId(companyId);

    if(signalsList != null) {
      return ResponseEntity.ok().body(signalsList);
    }

    return ResponseEntity.notFound().build();
  }

}