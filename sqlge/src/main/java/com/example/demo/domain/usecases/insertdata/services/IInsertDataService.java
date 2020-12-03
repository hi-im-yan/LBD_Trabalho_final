package com.example.demo.domain.usecases.insertdata.services;

import javax.sql.DataSource;

public interface IInsertDataService {
    void run(DataSource dataSource);
}
