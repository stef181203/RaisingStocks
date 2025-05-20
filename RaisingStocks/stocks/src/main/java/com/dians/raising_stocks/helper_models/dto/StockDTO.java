package com.dians.raising_stocks.helper_models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
  private String date;
  private LocalDate originalDate;
  private String lastTransactionPrice;
  private String maxPrice;
  private String minPrice;
  private String averagePrice;
  private String averagePercentage;
  private Integer quantity;
  private String turnoverInBestDenars;
  private String totalTurnoverInDenars;
}
