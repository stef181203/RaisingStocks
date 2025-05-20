package com.dians.raising_stocks.controller;

import com.dians.raising_stocks.helper_models.dto.CompanyDTO;
import com.dians.raising_stocks.service.CompanyCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyCommunicationService companyCommunicationService;

    @GetMapping
    /* Fetches a page of CompanyDTO objects based on the request parameters through
    * the company communication service directly from data-service (microservice). */
    ResponseEntity<Map<String, Object>> getCompanies(@RequestParam int page,
                                                     @RequestParam int pageSize,
                                                     @RequestParam String sort) {
        Map<String, Object> response = this.companyCommunicationService.fetchAllCompaniesDTOToPage(page, pageSize, sort);

        if(response != null) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    /* Fetches a page of all available CompanyDTO objects through the company
    * communication service directly from data-service (microservice). */
    public ResponseEntity<List<CompanyDTO>> getAllCompanyCodes() {
        List<CompanyDTO> codes = this.companyCommunicationService.fetchAllCompaniesDTO();

        if (codes != null) {
            return ResponseEntity.ok().body(codes);
        }

        return ResponseEntity.notFound().build();
    }
}