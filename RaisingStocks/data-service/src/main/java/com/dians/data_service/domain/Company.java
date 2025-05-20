package com.dians.data_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    private String name;
    private String latestWrittenDate;
    private boolean isStockHistoryEmpty;
    @OneToMany(mappedBy = "company")
    private List<StockDetailsHistory> stockHistory;

    public Company(String code, String name) {
        this.code = code;
        this.name = name;
        this.isStockHistoryEmpty = true;
        this.latestWrittenDate = "";
    }

    /* Updates the information about the stocks for a specific company
    * and saves the latest date for which a stock is available to a private variable. */
    public void updateStockInfo(String date) {
        this.latestWrittenDate = date;
        this.isStockHistoryEmpty = false;
    }
}