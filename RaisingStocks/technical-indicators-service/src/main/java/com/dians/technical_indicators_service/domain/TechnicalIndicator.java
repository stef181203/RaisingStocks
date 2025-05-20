package com.dians.technical_indicators_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalIndicator {
  public String code;
  public String name;
  public BigDecimal valueByDay;
  public BigDecimal valueByWeek;
  public BigDecimal valueByMonth;
  public boolean dayDataEnough;
  public boolean weekDataEnough;
  public boolean monthDataEnough;
  public String shortTermSignal;
  public String longTermSignal;

  public TechnicalIndicator(String code, String name) {
    this.code = code;
    this.name = name;
    this.shortTermSignal = "";
    this.longTermSignal = "";
  }
}