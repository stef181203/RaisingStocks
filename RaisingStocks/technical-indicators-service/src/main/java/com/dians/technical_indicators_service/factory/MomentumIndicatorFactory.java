package com.dians.technical_indicators_service.factory;

import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;
import com.dians.technical_indicators_service.factory.helpers.CCIHelper;
import com.dians.technical_indicators_service.factory.helpers.IndicatorHelper;
import com.dians.technical_indicators_service.factory.helpers.RSIHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MomentumIndicatorFactory extends TechnicalIndicatorFactory {
    public MomentumIndicatorFactory(List<StockValues> stocks) {
        super(stocks);
    }

    @Override
    /* This method creates the momentum indicators and
     * calls functions for their value and signal calculations. */
    public TechnicalIndicator createIndicator(String code, String name) {
        TechnicalIndicator indicator = new TechnicalIndicator(code, name);
        IndicatorHelper.checkIfIssuerHasEnoughStocksForTechnicalIndicator(this.stocks, indicator);

        switch (code) {
            case "RSI": {
                indicator.setValueByDay(getRSIValueByTimeframe(indicator.isDayDataEnough(), 2));
                indicator.setValueByWeek(getRSIValueByTimeframe(indicator.isWeekDataEnough(), 7));
                indicator.setValueByMonth(getRSIValueByTimeframe(indicator.isMonthDataEnough(), 30));
            } break;
            case "ROC": {
                indicator.setValueByDay(getROCValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getROCValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getROCValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "%R": {
                indicator.setValueByDay(getWPRValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getWPRValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getWPRValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "%K": {
                indicator.setValueByDay(getSTOValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getSTOValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getSTOValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
            case "CCI": {
                indicator.setValueByDay(getCCIValueByTimeframe(indicator.isDayDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 2)));
                indicator.setValueByWeek(getCCIValueByTimeframe(indicator.isWeekDataEnough(), IndicatorHelper.getLastNStocks(this.stocks, 7)));
                indicator.setValueByMonth(getCCIValueByTimeframe(indicator.isMonthDataEnough(), this.stocks));
            } break;
        }

        indicator.setShortTermSignal(getSignal(code, indicator.getValueByWeek().doubleValue()));
        indicator.setLongTermSignal(getSignal(code, indicator.getValueByMonth().doubleValue()));

        return indicator;
    }

    /* Below this comment every momentum indicator has its own
     * formula, so we need multiple functions (similar or not) for
     * their correct value calculation. */

    // Calculates RSI by given timeframe.
    private BigDecimal getRSIValueByTimeframe(boolean hasEnoughData, int numberOfDays) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        List<BigDecimal> gains = new ArrayList<>();
        List<BigDecimal> losses = new ArrayList<>();

        RSIHelper.calculateGainsAndLossesForRSI(this.stocks, gains, losses);

        double averageGains = RSIHelper.calculateAverageValueForRSI(IndicatorHelper.getLastNValues(gains, numberOfDays));
        double averageLosses = RSIHelper.calculateAverageValueForRSI(IndicatorHelper.getLastNValues(losses, numberOfDays));

        if(averageGains == 0 && averageLosses == 0) {
            return new BigDecimal(50);
        }
        else if(averageGains != 0 && averageLosses == 0) {
            return new BigDecimal(100);
        }
        else {
            double RS = averageGains / averageLosses;
            double RSIvalue = 100 - (100 / (1 + RS));
            return new BigDecimal(RSIvalue).setScale(2, RoundingMode.DOWN);
        }
    }

    // Calculates ROC by given timeframe
    private BigDecimal getROCValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        double currentPrice = stocks.getLast().getLastTransactionPrice().doubleValue();
        double previousPrice = stocks.getFirst().getLastTransactionPrice().doubleValue();
        double result = (currentPrice - previousPrice) / previousPrice * 100;
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }

    // Calculates WPR (%R) by given timeframe.
    private BigDecimal getWPRValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        double close = stocks.getLast().getLastTransactionPrice().doubleValue();
        double highestHigh = IndicatorHelper.getHighestHighForStocks(stocks);
        double lowestLow = IndicatorHelper.getLowestLowForStocks(stocks);

        if(highestHigh == lowestLow) {
            return new BigDecimal(0);
        }

        double result = (highestHigh - close) / (highestHigh - lowestLow) * -100;
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }

    // Calculates STO (%K) by given timeframe.
    private BigDecimal getSTOValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        double currentClose = stocks.getLast().getLastTransactionPrice().doubleValue();
        double highestHigh = IndicatorHelper.getHighestHighForStocks(stocks);
        double lowestLow = IndicatorHelper.getLowestLowForStocks(stocks);

        if(highestHigh == lowestLow) {
            return new BigDecimal(0);
        }

        double result = (currentClose - lowestLow) / (highestHigh - lowestLow) * 100;
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }

    // Calculates CCI by given timeframe.
    private BigDecimal getCCIValueByTimeframe(boolean hasEnoughData, List<StockValues> stocks) {
        if(!hasEnoughData) {
            return new BigDecimal(-999);
        }

        List<Double> typicalPrices = CCIHelper.calculateTypicalPricesForGivenStocks(stocks);
        double SMA = IndicatorHelper.calculateSMAForGivenPrices(typicalPrices);
        double meanDeviation = CCIHelper.calculateMeanDeviationForCCI(typicalPrices, SMA);

        if(meanDeviation == 0) {
            return new BigDecimal(0);
        }

        double lastTP = typicalPrices.getLast();

        double result = (lastTP - SMA) / (0.015 * meanDeviation);
        return new BigDecimal(result).setScale(2, RoundingMode.DOWN);
    }

    /* This method returns a signal (Buy, Hold, Sell) based on
     * the momentum indicator, the specified signal calculation formula
     * and the value comparing to the signal formula. */
    private String getSignal(String code, double value) {
        String signal = "";
        /* Every momentum indicator has different signal calculation */
        switch (code) {
            case "RSI": {
                if(value < 30) {
                    signal = "Buy";
                }
                else if(value > 70) {
                    signal = "Sell";
                }
                else {
                    signal = "Hold";
                }
            } break;
            case "ROC": {
                if(value < -5) {
                    signal = "Sell";
                }
                else if(value > 5) {
                    signal = "Buy";
                }
                else {
                    signal = "Hold";
                }
            } break;
            case "%R": {
                if(value < -80) {
                    signal = "Buy";
                }
                else if(value > -20) {
                    signal = "Sell";
                }
                else {
                    signal = "Hold";
                }
            } break;
            case "%K": {
                if(value < 20) {
                    signal = "Buy";
                }
                else if(value > 80) {
                    signal = "Sell";
                }
                else {
                    signal = "Hold";
                }
            } break;
            case "CCI": {
                if(value < -100) {
                    signal = "Buy";
                }
                else if(value > 100) {
                    signal = "Sell";
                }
                else {
                    signal = "Hold";
                }
            } break;
            default: break;
        }

        return signal;
    }
}
