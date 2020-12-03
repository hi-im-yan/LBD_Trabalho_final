package com.example.demo.utils;

import com.example.demo.domain.entities.Table;

import java.util.Iterator;
import java.util.Map;

public abstract class GenerateSqlTables {

    public static String run(final Map tables) {
        StringBuilder query = new StringBuilder();
        tables.keySet().forEach(tableName -> {
            query.append("CREATE TABLE IF NOT EXISTS ".concat((String) tableName).concat(" (\n"));
            Table thisTable = (Table) tables.get(tableName);
            Iterator columns = thisTable.getColumns().keySet().stream().iterator();

            String columnName = (String) columns.next();
            String lastColumn = "\t".concat(columnName).concat(" VARCHAR(256)").concat("\n");

            while(columns.hasNext()) {
                columnName = (String) columns.next();
                query.append("\t".concat(columnName).concat(" VARCHAR(256)").concat(",\n"));
            }

            query.append(lastColumn).append(");\n\n");
        });

        return query.toString();
    }
}