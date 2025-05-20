package com.dians.technical_indicators_service.service.impl;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;
import com.dians.technical_indicators_service.factory.MomentumIndicatorFactory;
import com.dians.technical_indicators_service.factory.TrendIndicatorFactory;
import com.dians.technical_indicators_service.factory.helpers.IndicatorHelper;
import com.dians.technical_indicators_service.service.TechnicalIndicatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TechnicalIndicatorServiceImpl implements TechnicalIndicatorService {
  private List<StockValues> stocks;

  @Override
  public void setStocks(List<StockValues> stocks) {
    this.stocks = stocks;
  }

  @Override
  /* Creates 5 trend indicators, sets their values and returns them as a list. */
  public List<TechnicalIndicator> getTrendIndicators() {
    List<TechnicalIndicator> trendIndicators = new ArrayList<>();
    TrendIndicatorFactory trendFactory = new TrendIndicatorFactory(this.stocks);

    trendIndicators.add(trendFactory.createIndicator("SMA", "Simple Moving Average"));
    trendIndicators.add(trendFactory.createIndicator("EMA", "Exponential Moving Average"));
    trendIndicators.add(trendFactory.createIndicator("HMA", "Hull Moving Average"));
    trendIndicators.add(trendFactory.createIndicator("VWMA", "Volume Weighted Moving Average"));
    trendIndicators.add(trendFactory.createIndicator("IBL", "Ichimoku Baseline"));

    return trendIndicators;
  }

  @Override
  /* Creates 5 momentum indicators, sets their values and returns them as a list. */
  public List<TechnicalIndicator> getMomentumIndicators() {
    List<TechnicalIndicator> momentumIndicators = new ArrayList<>();
    MomentumIndicatorFactory momentumFactory = new MomentumIndicatorFactory(this.stocks);

    momentumIndicators.add(momentumFactory.createIndicator("RSI", "Relative Strength Index"));
    momentumIndicators.add(momentumFactory.createIndicator("ROC", "Rate of Change"));
    momentumIndicators.add(momentumFactory.createIndicator("%R", "Williams Percent Range"));
    momentumIndicators.add(momentumFactory.createIndicator("%K", "Stochastic Oscillator"));
    momentumIndicators.add(momentumFactory.createIndicator("CCI", "Commodity Channel Index"));

    return momentumIndicators;
  }

  @Override
  /* This method sums up a final signal based on the individual
   * signal of each trend indicator and on the timeframe (short, long). */
  public String getSignalFromTrendIndicatorsByTimeframe(double currentPrice, double smaValue, double emaValue, double hmaValue, double vwmaValue, double ichimokuBaselineValue) {
    if(smaValue == 0) {
      return "Hold";
    }

    List<Boolean> signalsList = new ArrayList<>();
    signalsList.add(currentPrice > smaValue);
    signalsList.add(currentPrice > emaValue);
    signalsList.add(currentPrice > hmaValue);
    signalsList.add(currentPrice > vwmaValue);
    signalsList.add(currentPrice > ichimokuBaselineValue);

    int signalSum = signalsList.stream().map(v -> v ? 1 : 0).mapToInt(Integer::intValue).sum();

    boolean isPriceTrendingUp = hmaValue > emaValue && emaValue > smaValue;
    boolean isPriceTrendingDown = hmaValue < emaValue && emaValue < smaValue;

    if(signalSum >= 4 || signalSum == 3 && isPriceTrendingUp) {
      return "Buy";
    }
    else if(signalSum < 2 || signalSum == 2 && isPriceTrendingDown) {
      return "Sell";
    }
    else {
      return "Hold";
    }
  }

  @Override
  /* This method sums up the final signal by combining and comparing the
   * 5 momentum indicator signals and the one summed up signal for the 5 trend indicators. */
  public String getFinalSignalByTimeframe(int timeframe, int numberOfStocksAvailable, List<TechnicalIndicator> indicatorsList, List<TechnicalIndicator> oscillatorsList) {
    if(numberOfStocksAvailable < timeframe) {
      return "Not Available";
    }

    List<String> oscillatorSignals = new ArrayList<>();
    for(TechnicalIndicator oscillator : oscillatorsList) {
      if(timeframe == 7) {
        oscillatorSignals.add(oscillator.getShortTermSignal());
      }
      else {
        oscillatorSignals.add(oscillator.getLongTermSignal());
      }
    }

    String indicatorsSignal;
    if(timeframe == 7) {
      indicatorsSignal = getSignalFromTrendIndicatorsByTimeframe(
          this.stocks.getLast().getLastTransactionPrice().doubleValue(),
          indicatorsList.get(0).getValueByWeek().doubleValue(),
          indicatorsList.get(1).getValueByWeek().doubleValue(),
          indicatorsList.get(2).getValueByWeek().doubleValue(),
          indicatorsList.get(3).getValueByWeek().doubleValue(),
          indicatorsList.get(4).getValueByWeek().doubleValue());
    }
    else {
      indicatorsSignal = getSignalFromTrendIndicatorsByTimeframe(
          this.stocks.getLast().getLastTransactionPrice().doubleValue(),
          indicatorsList.get(0).getValueByMonth().doubleValue(),
          indicatorsList.get(1).getValueByMonth().doubleValue(),
          indicatorsList.get(2).getValueByMonth().doubleValue(),
          indicatorsList.get(3).getValueByMonth().doubleValue(),
          indicatorsList.get(4).getValueByMonth().doubleValue());
    }

    Map<String, Integer> signalsMap = new HashMap<>();
    signalsMap.put("Hold", 0);
    signalsMap.put("Buy", 0);
    signalsMap.put("Sell", 0);

    signalsMap.computeIfPresent(indicatorsSignal, (key, value) -> value + 1);

    for(String signal : oscillatorSignals) {
      signalsMap.computeIfPresent(signal, (key, value) -> value + 1);
    }

    String finalSignal = "Hold";
    int max = signalsMap.get(finalSignal);
    int buyCount = signalsMap.get("Buy");
    int sellCount = signalsMap.get("Sell");
    boolean duplicates = false;

    if(buyCount == max) {
      duplicates = true;
    }
    else if(buyCount > max) {
      max = buyCount;
      finalSignal = "Buy";
    }

    if(sellCount == max && duplicates) {
      return finalSignal;
    }
    else if(sellCount > max){
      finalSignal = "Sell";
    }

    return finalSignal;
  }

  @Override
  /* This method returns a list of two signals,
   * one for short term trading and one for long term. */
  public List<String> getFinalSignalsList() {
    List<String> finalSignals = new ArrayList<>();
    finalSignals.add(getFinalSignalByTimeframe(7, IndicatorHelper.getLastNStocks(this.stocks, 7).size(), getTrendIndicators(), getMomentumIndicators()));
    finalSignals.add(getFinalSignalByTimeframe(30, IndicatorHelper.getLastNStocks(this.stocks, 30).size(), getTrendIndicators(), getMomentumIndicators()));
    return finalSignals;
  }

}