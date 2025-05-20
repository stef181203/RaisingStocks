package com.dians.raising_stocks.controller;

import com.dians.raising_stocks.service.DBCommunicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBController {
    private final DBCommunicationService dbCommunicationService;

    public DBController(DBCommunicationService dbCommunicationService) {
        this.dbCommunicationService = dbCommunicationService;
    }

    @PostMapping("/api/update-database")
    /* Sends a signal to data-service (microservice) through the
    * DB communications that tells data-service to update the database. */
    public void updateDatabase() {
        this.dbCommunicationService.sendSignalForDatabaseUpdate();
    }
}
