package com.dians.technical_indicators_service.service.impl;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.service.StocksCommunicationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class StocksCommunicationServiceImpl implements StocksCommunicationService {
  private final WebClient.Builder webClientBuilder;
  private final String dataServiceName = "data-service";

  public StocksCommunicationServiceImpl(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  /* Fetches the last 30 stocks for a given company id (if available)
  * from the data-service (microservice) and returns them. */
  public List<StockValues> fetchLast30StocksForCompanyId(Long companyId) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/stocks/last-30?companyId=%d", dataServiceName, companyId))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StockValues>>() {})
        .block();
  }
}
