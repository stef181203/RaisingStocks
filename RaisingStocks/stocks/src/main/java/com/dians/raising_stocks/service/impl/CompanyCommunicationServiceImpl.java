package com.dians.raising_stocks.service.impl;

import com.dians.raising_stocks.helper_models.dto.CompanyDTO;
import com.dians.raising_stocks.service.CompanyCommunicationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class CompanyCommunicationServiceImpl implements CompanyCommunicationService {

  private final WebClient.Builder webClientBuilder;
  private final String dataServiceName = "data-service";

  public CompanyCommunicationServiceImpl(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  /* Sends an API get request to data-service (microservice) and retrieves
  * a page of CompanyDTO objects based on the given function parameters. */
  public Map<String, Object> fetchAllCompaniesDTOToPage(int page, int pageSize, String sort) {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/companies?page=%d&pageSize=%d&sort=%s", dataServiceName, page, pageSize, sort))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();
  }

  @Override
  /* Sends an API get request to data-service (microservice) and retrieves
  * a page of all CompanyDTO objects available in the database. */
  public List<CompanyDTO> fetchAllCompaniesDTO() {
    return webClientBuilder.build()
        .get()
        .uri(String.format("http://%s/api/companies/all", dataServiceName))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<CompanyDTO>>() {})
        .block();
  }

}