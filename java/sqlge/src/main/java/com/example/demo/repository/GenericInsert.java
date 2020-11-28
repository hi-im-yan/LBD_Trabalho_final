package com.example.demo.repository;

import com.example.demo.utils.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class GenericInsert {

    private Boolean columnIsATable(Map entity, String fieldName) {
        return Boolean.TRUE.equals(isColumnIsATable((Optional.ofNullable(entity.get(fieldName)).orElse(new Object()).getClass().getSimpleName())))
                || Boolean.TRUE.equals(isColumnIsATable(fieldName));
    }

    private Boolean isColumnIsATable(String fieldType) {
        return StringUtils.containWords(fieldType, new String[]{"List", "Set", "Map", "Tree", "Queue", "motivos", "vices"});
    }

    @Async
    public CompletableFuture<Void> insert(DataSource datasource, String tableName, Map entity) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        Iterator params = entity.keySet().stream().iterator();
        String fieldName = (String) params.next();

        columns.append(fieldName);
        String column = (entity.get(fieldName) != null ? entity.get(fieldName).toString() : "null").replace("'", " ");
        values.append("'".concat(column).concat("'"));
        while (params.hasNext()) {
            fieldName = (String) params.next();
            if (Boolean.FALSE.equals(columnIsATable(entity, fieldName))) {
                columns.append(",".concat(fieldName));
                values.append(",'".concat(column).concat("'"));
            }
        }

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ".concat(tableName).concat(" "));
        query.append("(".concat(columns.toString()).concat(") VALUES "));
        query.append("(".concat(values.toString()).concat(");"));

        try {
            Connection connection = datasource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }
}
