package com.dians.data_service.helper_models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long companyId;
    private String code;
    private String name;
    private String latestTurnoverDate;
}