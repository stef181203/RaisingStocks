package com.dians.technical_indicators_service.service;

import com.dians.technical_indicators_service.domain.StockValues;

import java.util.List;

public interface StocksCommunicationService {
  List<StockValues> fetchLast30StocksForCompanyId(Long companyId);
}
