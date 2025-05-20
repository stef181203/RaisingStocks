package com.dians.technical_indicators_service.service;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;

import java.util.List;

public interface TechnicalIndicatorService {
  /* Short explanation for the methods that need one is located
   * above the method implementations in the service implementation. */
  void setStocks(List<StockValues> stocks);
  List<TechnicalIndicator> getTrendIndicators();
  List<TechnicalIndicator> getMomentumIndicators();
  String getSignalFromTrendIndicatorsByTimeframe(double currentPrice, double smaValue, double emaValue, double hmaValue, double vwmaValue, double ichimokuBaselineValue);
  String getFinalSignalByTimeframe(int timeframe, int numberOfStocksAvailable, List<TechnicalIndicator> indicatorsList, List<TechnicalIndicator> oscillatorsList);
  List<String> getFinalSignalsList();
}