package com.example.demo.domain.usecases.generatetables.services;

import javax.sql.DataSource;

public interface IGenerateTablesService {
    void run(DataSource dataSource);
}
