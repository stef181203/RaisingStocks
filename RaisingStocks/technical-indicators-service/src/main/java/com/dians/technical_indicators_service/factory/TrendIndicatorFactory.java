package com.dians.technical_indicators_service.factory;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;
import com.dians.technical_indicators_service.factory.helpers.HMAHelper;
import com.dians.technical_indicators_service.factory.helpers.IndicatorHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class TrendIndicatorFactory extends TechnicalIndicatorFactory {

    public TrendIndicatorFactory(List<StockValues> stocks) {
        super(stocks);
    }

    @Override
    /* This method creates the momentum indicators and
     * calls functions for their value calculations. */
    public TechnicalIndicator createIndicator(String code, String name) {
        TechnicalIndicator indicator = new TechnicalIndicator(code, name);
        IndicatorHelper.checkIfIssuerHasEnoughStocksForTechnicalIndicator(this.stocks, indicator);

        switch (code) {
            case "SMA": {
                indicator.setValueByDay(getSMAValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getSMAValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getSMAValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "EMA": {
                indicator.setValueByDay(getEMAValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getEMAValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getEMAValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "VWMA": {
                indicator.setValueByDay(getVWMAValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getVWMAValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getVWMAValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "HMA": {
                indicator.setValueByDay(getHMAValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getHMAValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getHMAValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "IBL": {
                indicator.setValueByDay(getIBLValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getIBLValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getIBLValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            default: break;
        }

        return indicator;
    }

    /* Below this comment every momentum indicator has its own
     * formula, so we need multiple functions (similar or not) for
     * their correct value calculation. */

    // Calculates SMA for a given timeframe.
    private BigDecimal getSMAValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        List<Double> prices = stocks.stream()
                .map(StockValues::getLastTransactionPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .boxed()
                .toList();
        double result = IndicatorHelper.calculateSMAForGivenPrices(prices);
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }

    // Calculates EMA for a given timeframe.
    private BigDecimal getEMAValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        int size = stocks.size();
        double k = 2.0 / (size + 1);
        double EMA = stocks.getFirst().getLastTransactionPrice().doubleValue();
        for(StockValues stock : stocks) {
            EMA = stock.getLastTransactionPrice().doubleValue() * k + (EMA * (1 - k));
        }

        return new BigDecimal(EMA).setScale(2, RoundingMode.DOWN);
    }

    // Calculates VWMA for a given timeframe.
    private BigDecimal getVWMAValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        double totalWeightedPrice = 0;
        double totalVolume = 0;

        for(StockValues stock : stocks) {
            totalWeightedPrice += (stock.getLastTransactionPrice().doubleValue() * stock.getQuantity());
            totalVolume += stock.getQuantity();
        }

        double result = totalVolume != 0 ? totalWeightedPrice / totalVolume : 0;
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }

    // Calculates HMA for a given timeframe.
    private BigDecimal getHMAValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        List<Double> stockPrices = stocks.stream().map(s -> s.getLastTransactionPrice().doubleValue()).toList();
        List<Double> rawHMAList = new ArrayList<>();
        List<Double> hmaList = new ArrayList<>();

        int period = 9;
        int halfPeriod = (int) (period / 2.0);
        int sqrtPeriod = (int) Math.sqrt(period);

        for(int i=0; i<stockPrices.size(); i++) {
            List<Double> pricesSubList = stockPrices.subList(0, i + 1);

            double halfWMA = HMAHelper.calculateWMA(pricesSubList, halfPeriod);
            double fullWMA = HMAHelper.calculateWMA(pricesSubList, period);
            double rawHMA = 2 * halfWMA - fullWMA;

            rawHMAList.add(rawHMA);

            double hma = HMAHelper.calculateWMA(rawHMAList, sqrtPeriod);
            hmaList.add(hma);
        }

        return BigDecimal.valueOf(hmaList.getLast()).setScale(2, RoundingMode.DOWN);
    }

    // Calculates IBL for a given timeframe.
    private BigDecimal getIBLValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        double highestHigh = IndicatorHelper.getHighestHighForStocks(stocks);
        double lowestLow = IndicatorHelper.getLowestLowForStocks(stocks);

        double result = (highestHigh + lowestLow) / 2;
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }
}
