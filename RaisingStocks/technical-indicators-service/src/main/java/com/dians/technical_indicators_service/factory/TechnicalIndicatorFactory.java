package com.dians.technical_indicators_service.factory;


import com.dians.technical_indicators_service.domain.StockValues;
import com.dians.technical_indicators_service.domain.TechnicalIndicator;

import java.util.List;

public abstract class TechnicalIndicatorFactory {
    /* This is an abstract class setting the blueprint
    * for the two factories that are extending it. */
    protected List<StockValues> stocks;

    public TechnicalIndicatorFactory(List<StockValues> stocks) {
        this.stocks = stocks;
    }

    public abstract TechnicalIndicator createIndicator(String code, String name);

}
