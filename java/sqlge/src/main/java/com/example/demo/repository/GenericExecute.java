package com.example.demo.repository;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class GenericExecute {

    public void run(DataSource datasource, String query) {
        try {
            Connection connection = datasource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DROP SCHEMA public CASCADE;\n" +
                    "            CREATE SCHEMA public;\n" +
                    "            GRANT ALL ON SCHEMA public TO root;\n" +
                    "            GRANT ALL ON SCHEMA public TO public;");
            statement.execute(query);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
