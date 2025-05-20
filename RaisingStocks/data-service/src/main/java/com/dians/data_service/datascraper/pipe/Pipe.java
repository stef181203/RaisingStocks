package com.dians.data_service.datascraper.pipe;

import com.dians.data_service.datascraper.filters.IFilter;
import com.dians.data_service.datascraper.filters.impl.CompaniesFilter;
import com.dians.data_service.datascraper.filters.impl.DateFilter;
import com.dians.data_service.datascraper.filters.impl.InformationFilter;
import com.dians.data_service.datascraper.filters.impl.RemoveFilter;
import com.dians.data_service.service.CompanyService;
import com.dians.data_service.service.StockDetailsService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Pipe {
  Map<String, String> codesAndDates;
  List<IFilter<String, String>> filters;
  private final CompanyService companyService;
  private final StockDetailsService stockDetailsService;

  public Pipe(StockDetailsService stockDetailsService, CompanyService companyService) {
    this.companyService = companyService;
    this.stockDetailsService = stockDetailsService;
    this.codesAndDates = new HashMap<>();
    this.filters = new ArrayList<>();
  }

  // This method creates the filters in order of execution.
  public void createFilters() {
    filters.add(new CompaniesFilter(this.companyService));
    filters.add(new DateFilter(this.stockDetailsService, this.companyService));
    filters.add(new RemoveFilter(this.companyService));
    filters.add(new InformationFilter(this.stockDetailsService, this.companyService));
  }

  // This method starts the execution from the first filter in the list.
  public void executeFilters() throws IOException {
    for(IFilter<String, String> filter : filters) {
      this.codesAndDates = filter.execute(this.codesAndDates);
    }
  }

}
