package com.example.demo.repository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

@Component
public class GenericExecute {

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CompletableFuture<?> run(DataSource datasource, String query) {
        try {
            Connection connection = datasource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(query);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}

