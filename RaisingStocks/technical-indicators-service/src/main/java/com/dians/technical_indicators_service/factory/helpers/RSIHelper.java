package com.dians.technical_indicators_service.factory.helpers;


import com.dians.technical_indicators_service.domain.StockValues;

import java.math.BigDecimal;
import java.util.List;

public class RSIHelper {

    /* Calculates average value starting from the second
    * value from the list due to our RSI calculation logic. */
    public static double calculateAverageValueForRSI(List<BigDecimal> list) {
        return list.stream().skip(1).mapToDouble(BigDecimal::doubleValue).sum() / (list.size());}

    // Calculates gains and losses for each day of the given stocks.
    public static void calculateGainsAndLossesForRSI(List<StockValues> stocks, List<BigDecimal> gains, List<BigDecimal> losses) {
        for(int i=0; i<stocks.size(); i++) {
            if(i != 0) {
                BigDecimal prevPrice = stocks.get(i - 1).getLastTransactionPrice();
                BigDecimal currPrice = stocks.get(i).getLastTransactionPrice();

                if(currPrice.compareTo(prevPrice) < 0) {
                    losses.add(prevPrice.subtract(currPrice));
                    gains.add(BigDecimal.ZERO);
                }
                else if(currPrice.compareTo(prevPrice) > 0) {
                    gains.add(currPrice.subtract(prevPrice));
                    losses.add(BigDecimal.ZERO);
                }
                else {
                    gains.add(BigDecimal.ZERO);
                    losses.add(BigDecimal.ZERO);
                }
            }
        }
    }
}
