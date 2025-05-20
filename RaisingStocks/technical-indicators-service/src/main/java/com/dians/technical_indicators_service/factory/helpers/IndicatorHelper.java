package com.dians.technical_indicators_service.factory.helpers;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IndicatorHelper {

    // Returns the last 'count' stocks from the given list.
    public static List<StockValues> getLastNStocks(List<StockValues> stocks, int count) {
        if (stocks == null || stocks.isEmpty() || count <= 1) {
            return new ArrayList<>();
        }

        int size = stocks.size();
        return stocks.subList(Math.max(size - count, 0), size);
    }

    // Returns the last 'count' values from the given list.
    public static List<BigDecimal> getLastNValues(List<BigDecimal> list, int count) {
        int size = list.size();
        return list.subList(Math.max(size - count, 0), size);
    }

    /* Sets the crucial variables in the given TechnicalIndicator that
     * tell us if a stock has enough data for calculating the indicators. */
    public static void checkIfIssuerHasEnoughStocksForTechnicalIndicator(List<StockValues> stocks, TechnicalIndicator indicator) {
        if(stocks.size() < 2) {
            indicator.setDayDataEnough(false);
            indicator.setWeekDataEnough(false);
            indicator.setMonthDataEnough(false);
        }
        else if(stocks.size() < 7) {
            indicator.setDayDataEnough(true);
            indicator.setWeekDataEnough(false);
            indicator.setMonthDataEnough(false);
        }
        else if(stocks.size() < 30) {
            indicator.setDayDataEnough(true);
            indicator.setWeekDataEnough(true);
            indicator.setMonthDataEnough(false);
        }
        else {
            indicator.setDayDataEnough(true);
            indicator.setWeekDataEnough(true);
            indicator.setMonthDataEnough(true);
        }
    }

    // Returns the highest maximum price for the given stocks.
    public static double getHighestHighForStocks(List<StockValues> stocks) {
        double highestHigh = stocks.getFirst().getMaxPrice().doubleValue();

        for(int i=1; i<stocks.size(); i++) {
            highestHigh = Math.max(highestHigh, stocks.get(i).getMaxPrice().doubleValue());
        }
        return highestHigh;
    }

    // Returns the lowest minimum price for the given stocks.
    public static double getLowestLowForStocks(List<StockValues> stocks) {
        double lowestLow = stocks.getFirst().getMinPrice().doubleValue();

        for(int i=1; i<stocks.size(); i++) {
            lowestLow = Math.min(lowestLow, stocks.get(i).getMinPrice().doubleValue());
        }
        return lowestLow;
    }

    // Calculates SMA for the given prices.
    public static double calculateSMAForGivenPrices(List<Double> prices) {
        return prices.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

}
