package com.dians.technical_indicators_service.factory.helpers;

import java.util.List;

public class HMAHelper {

    // Calculates Weighted moving average for given stockPrices
    public static double calculateWMA(List<Double> stockPrices, int period) {
        double sum = 0;
        int effectivePeriod = Math.min(stockPrices.size(), period);

        for(int i=0; i<effectivePeriod; i++) {
            sum += stockPrices.get(effectivePeriod - 1 - i) * (effectivePeriod - i);
        }

        return sum / ((effectivePeriod * (effectivePeriod + 1)) / 2.0);
    }

}
