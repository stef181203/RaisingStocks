package com.dians.data_service.datascraper.filters.impl;

import com.dians.data_service.datascraper.filters.IFilter;
import com.dians.data_service.domain.Company;
import com.dians.data_service.service.CompanyService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class CompaniesFilter implements IFilter<String, String> {
  private static final String codesURL = "https://www.mse.mk/mk/stats/symbolhistory/kmb";
  private static final String namesURL = "https://www.mse.mk/en/symbol/";
  private final CompanyService companyService;

  public CompaniesFilter(CompanyService companyService) {
    this.companyService = companyService;
  }

  @Override
  /* This method extracts the company codes from the web page that we are scraping
   * and puts them in a map. The map keys are the company codes and each value is the latest
   * date for the stock of the given company. We need that for the further logic. */
  public Map<String, String> execute(Map<String, String> companies) throws IOException {
    Document codesDoc = Jsoup.connect(codesURL).get();

    Elements elements = codesDoc.select(".dropdown option");
    for(Element el : elements) {
      String code = el.text();
      if(!code.matches(".*\\d.*")) {
        Document nameDoc = Jsoup.connect(namesURL + code).get();
        String name = nameDoc.select(".title").text();
        if(name == null || name.isEmpty()) {
          String[] parts = nameDoc.select("#titleKonf2011").text().split("-");
          name = parts[2].trim();
        }

        Optional<Company> companyOptional = this.companyService.findCompanyByCode(code);
        if(companyOptional.isEmpty()) {
          Company company = new Company(code, name);
          this.companyService.save(company);
          companies.put(code, "");
        }
        else {
          companies.put(code, companyOptional.get().getLatestWrittenDate());
        }
      }
    }

    return companies;
  }
}
