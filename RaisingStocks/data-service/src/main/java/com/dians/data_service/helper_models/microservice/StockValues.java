package com.dians.data_service.helper_models.microservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockValues {
  /* The idea behind yet another model class is that the
  * technical-indicators microservice doesn't have database communication
  * and the models must be different since they can't contain database related
  * annotations (@ManyToOne, @Entity, @Id, ...). That is why we created this model
  * so the microservice can accept it and finish its job. */
  private BigDecimal lastTransactionPrice;
  private BigDecimal maxPrice;
  private BigDecimal minPrice;
  private Integer quantity;
}
