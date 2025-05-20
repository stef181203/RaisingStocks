package com.dians.raising_stocks.service.impl;

import com.dians.raising_stocks.service.DBCommunicationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DBCommunicationServiceImpl implements DBCommunicationService {

  private final WebClient.Builder webClientBuilder;
  private final String dataServiceName = "data-service";

  public DBCommunicationServiceImpl(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }
  @Override
  /* Sends an API post request to data-service (microservice) that
  * starts the pipe and filter architecture and updates the database.*/
  public void sendSignalForDatabaseUpdate() {
    webClientBuilder.build()
        .post()
        .uri(String.format("http://%s/api/update-database", dataServiceName))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }
}
