package com.dians.data_service.service.impl;

import com.dians.data_service.datascraper.pipe.Pipe;
import com.dians.data_service.service.DatabaseService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService {
  private final Pipe pipe;

  public DatabaseServiceImpl(Pipe pipe) {
    this.pipe = pipe;
  }

  @Override
  /* Starts the creation and execution of the filters inside
   * the pipe in order to create or update the tables in the database. */
  public void updateDatabase() {
    try {
      pipe.createFilters();
      pipe.executeFilters();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
