package com.example.demo.repository;

import com.example.demo.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class GenericInsert {

    Logger logger = LoggerFactory.getLogger(GenericInsert.class);

    private Boolean columnIsATable(Map entity, String fieldName) {
        return Boolean.TRUE.equals(isColumnIsATable((Optional.ofNullable(entity.get(fieldName)).orElse(new Object()).getClass().getSimpleName())))
                || Boolean.TRUE.equals(isColumnIsATable(fieldName));
    }

    private Boolean isColumnIsATable(String fieldType) {
        return StringUtils.containWords(fieldType, new String[]{"List", "Set", "Map", "Tree", "Queue", "motivos", "vices"});
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CompletableFuture<?> insert(DataSource datasource, String tableName, Map entity) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        Iterator<?> params = new ArrayList<>(entity.keySet()).stream().iterator();
        String fieldName = (String) params.next();

        columns.append(fieldName);
        String column = (entity.get(fieldName) != null ? entity.get(fieldName).toString() : "null").replace("'", " ");
        values.append("'".concat(column).concat("'"));
        while (params.hasNext()) {
            fieldName = (String) params.next();
            if (Boolean.FALSE.equals(columnIsATable(entity, fieldName))) {
                column = (entity.get(fieldName) != null ? entity.get(fieldName).toString() : "null").replace("'", " ");
                columns.append(",".concat(fieldName));
                values.append(",'".concat(column).concat("'"));
            }
        }

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ".concat(tableName).concat(" "));
        query.append("(".concat(columns.toString()).concat(") VALUES "));
        query.append("(".concat(values.toString()).concat(");"));

        try {
            logger.info("insert data into table {}.", tableName);
            Connection connection = datasource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
