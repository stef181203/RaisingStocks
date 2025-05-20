package com.dians.data_service.controller;

import com.dians.data_service.service.DatabaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBController {
    private final DatabaseService databaseService;

    public DBController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/api/update-database")
    /* Calls the updateDatabase() method from the database service
    * in order to start the pipe and filter architecture. */
    public void updateDatabase() {
        this.databaseService.updateDatabase();
    }
}
