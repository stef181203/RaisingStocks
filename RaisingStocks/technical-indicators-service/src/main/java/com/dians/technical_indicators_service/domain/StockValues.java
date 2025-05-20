package com.dians.technical_indicators_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockValues {
  private BigDecimal lastTransactionPrice;
  private BigDecimal maxPrice;
  private BigDecimal minPrice;
  private Integer quantity;
}
