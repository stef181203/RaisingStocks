package com.dians.raising_stocks.service.impl;

import com.dians.raising_stocks.domain.TechnicalIndicator;
import com.dians.raising_stocks.helper_models.dto.StockGraphDTO;
import com.dians.raising_stocks.service.StockDetailsCommunicationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class StockDetailsCommunicationServiceImpl implements StockDetailsCommunicationService {

  private final WebClient.Builder webClientBuilder;
  private final String dataServiceName = "data-service";
  private final String technicalIndicatorsServiceName = "technical-indicators-service";

  public StockDetailsCommunicationServiceImpl(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  /* Sends an API get request to data-service (microservice)
  * and retrieves a Map containing a page of StockDTO objects,
  * total pages number and the company code based on the given function parameters. */
  public Map<String, Object> fetchAllStocksDTOByCompanyIdToPage(Long companyId, int page, int pageSize, String sort) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/stocks?companyId=%d&page=%d&pageSize=%d&sort=%s", dataServiceName, companyId, page, pageSize, sort))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();
  }

  @Override
  /* Sends an API get request to data-service (microservice) and retrieves
   * a list of StockGraphDTO objects based on the given function parameters. */
  public List<StockGraphDTO> fetchAllStockGraphDTOByCompanyIdAndYear(Long companyId, Integer year) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/stocks/graph?companyId=%d&year=%d", dataServiceName, companyId, year))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StockGraphDTO>>() {})
        .block();
  }

  @Override
  /* Sends an API get request to data-service (microservice) and retrieves
   * a list of all available years for a given stock based on the company id. */
  public List<Integer> fetchGraphYearsAvailable(Long companyId) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/stocks/graph-years?companyId=%d", dataServiceName, companyId))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Integer>>() {})
        .block();
  }

  @Override
  /* Sends an API get request to technical-indicator-service (microservice) and retrieves
   * a list of the trend indicators for a given stock based on the company id. */
  public List<TechnicalIndicator> fetchTrendIndicatorsByCompanyId(Long companyId) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/technical-indicators/trend?companyId=%d", technicalIndicatorsServiceName, companyId))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<TechnicalIndicator>>() {})
        .block();
  }

  @Override
  /* Sends an API get request to technical-indicator-service (microservice) and retrieves
   * a list of the momentum indicators for a given stock based on the company id. */
  public List<TechnicalIndicator> fetchMomentumIndicatorsByCompanyId(Long companyId) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/technical-indicators/momentum?companyId=%d", technicalIndicatorsServiceName, companyId))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<TechnicalIndicator>>() {})
        .block();
  }

  @Override
  /* Sends an API get request to technical-indicator-service (microservice)
   * and retrieves a list of the signals for a given stock based on the company id. */
  public List<String> fetchSignalsByCompanyId(Long companyId) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/technical-indicators/signals?companyId=%d", technicalIndicatorsServiceName, companyId))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
        .block();
  }

}