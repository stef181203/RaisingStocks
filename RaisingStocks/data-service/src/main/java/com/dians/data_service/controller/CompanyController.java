package com.dians.data_service.controller;

import com.dians.data_service.helper_models.dto.CompanyDTO;
import com.dians.data_service.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
        // Returns a page of companies based on the request parameters.
    ResponseEntity<Map<String, Object>> getCompanies(@RequestParam int page,
                                                     @RequestParam int pageSize,
                                                     @RequestParam String sort) {
        Map<String, Object> response = new HashMap<>();
        Page<CompanyDTO> companiesPage = this.companyService.findAllCompaniesDTOToPage(sort, page, pageSize);
        response.put("companies", companiesPage.get().collect(Collectors.toList()));
        response.put("totalPageCount", companiesPage.getTotalPages());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    // Returns all available company codes from the database.
    public ResponseEntity<List<CompanyDTO>> getAllCompanyCodes() {
        return ResponseEntity.ok().body(this.companyService.findAllCompaniesDTO());
    }
}