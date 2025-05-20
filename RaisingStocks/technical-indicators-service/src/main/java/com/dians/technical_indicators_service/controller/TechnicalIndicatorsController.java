package com.dians.technical_indicators_service.controller;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;
import com.dians.technical_indicators_service.service.StocksCommunicationService;
import com.dians.technical_indicators_service.service.TechnicalIndicatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technical-indicators")
public class TechnicalIndicatorsController {
  private final TechnicalIndicatorService technicalIndicatorService;
  private final StocksCommunicationService stocksCommunicationService;

  public TechnicalIndicatorsController(TechnicalIndicatorService technicalIndicatorService, StocksCommunicationService stocksCommunicationService) {
    this.technicalIndicatorService = technicalIndicatorService;
    this.stocksCommunicationService = stocksCommunicationService;
  }

  @GetMapping("/trend")
  /* Returns a list of the trend indicators
  * based on the stocks from the POST request. */
  public ResponseEntity<List<TechnicalIndicator>> getTrendIndicators(@RequestParam Long companyId) {
    List<StockValues> last30StocksList = this.stocksCommunicationService.fetchLast30StocksForCompanyId(companyId);

    if(last30StocksList == null) {
      return ResponseEntity.notFound().build();
    }

    this.technicalIndicatorService.setStocks(last30StocksList);
    return ResponseEntity.ok().body(this.technicalIndicatorService.getTrendIndicators());
  }

  @GetMapping("/momentum")
  /* Returns a list of the momentum indicators based
  * on the stocks from the POST request. */
  public ResponseEntity<List<TechnicalIndicator>> getMomentumIndicators(@RequestParam Long companyId) {
    List<StockValues> last30StocksList = this.stocksCommunicationService.fetchLast30StocksForCompanyId(companyId);

    if(last30StocksList == null) {
      return ResponseEntity.notFound().build();
    }
    this.technicalIndicatorService.setStocks(last30StocksList);
    return ResponseEntity.ok().body(this.technicalIndicatorService.getMomentumIndicators());
  }

  @GetMapping("/signals")
  /* Returns the signals based on the technical indicators
  * calculated in the background based on the stocks from the POST request.*/
  public ResponseEntity<List<String>> getSignals(@RequestParam Long companyId) {
    List<StockValues> last30StocksList = this.stocksCommunicationService.fetchLast30StocksForCompanyId(companyId);

    if(last30StocksList == null) {
      return ResponseEntity.notFound().build();
    }
    this.technicalIndicatorService.setStocks(last30StocksList);
    return ResponseEntity.ok().body(this.technicalIndicatorService.getFinalSignalsList());
  }

}
