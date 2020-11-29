package com.example.demo.domain.usecases.generatetables.services;

import com.example.demo.domain.entities.Table;
import com.example.demo.domain.json.services.IParseJsons;
import com.example.demo.repository.GenericExecute;
import com.example.demo.utils.GenerateSqlTables;
import com.example.demo.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenerateTablesService implements IGenerateTablesService {

    private static final Map<String, Table> tables = new HashMap();
    private final GenericExecute genericExecute;
    private final IParseJsons parseJsons;

    public void run(DataSource dataSource) {
        Set<Map<String, Object>> candidates = parseJsons.run();
        candidates.forEach(candidate -> mountTable("candidatos", candidate));
        genericExecute.run(dataSource, GenerateSqlTables.run(tables));
    }

    private Table createTableWithIdSerial(final String name) {
        return Table.builder().name(name).columns(new HashMap<>()).build();
    }

    private Map<String, Object> mountBaseOfColumns(Map entity) {
        Map<String, Object> columns = new HashMap<>();
        entity.keySet().forEach(fieldName -> {
            if (Boolean.FALSE.equals(columnIsATableOrFromOtherTable(entity, (String) fieldName))) {
                columns.put((String) fieldName, "");
            }
        });

        return columns;
    }

    private Boolean columnIsATableOrFromOtherTable(Map entity, String fieldName) {
        return Boolean.TRUE.equals(isColumnIsATable((Optional.ofNullable(entity.get(fieldName)).orElse(new Object()).getClass().getSimpleName())))
                || Boolean.TRUE.equals(isColumnIsATable(fieldName));
    }

    private Boolean isColumnIsATable(String fieldType) {
        return StringUtils.containWords(fieldType, new String[]{"List", "Set", "Map", "Tree", "Queue", "motivos", "vices"});
    }

    private void mountTable(final String tableName, Map entity) {
        if (tables.containsKey(tableName) && tables.get(tableName) != null) {
            entity.keySet().forEach(fieldName -> {
                if (Boolean.FALSE.equals(columnIsATableOrFromOtherTable(entity, (String) fieldName))) {
                    if (!tables.get(tableName).getColumns().containsKey(fieldName)) {
                        tables.get(tableName).getColumns().put((String) fieldName, "");
                    }
                } else if (fieldName.equals("cargo") && entity.get("cargo") != null) {
                    mountTable("cargos", (Map) entity.get("cargo"));
                } else if (fieldName.equals("vices") && entity.get("vices") != null) {
                    ((List) entity.get("vices")).stream().forEach(vice -> {
                        mountTable("vices", (Map) vice);
                    });
                } else if (fieldName.equals("bens") && entity.get("bens") != null) {
                    ((List) entity.get("bens")).stream().forEach(bem -> {
                        mountTable("candidato_bens", (Map) bem);
                    });
                } else if (fieldName.equals("partido") && entity.get("partido") != null) {
                    mountTable("partidos", (Map) entity.get("partido"));
                } else if (fieldName.equals("eleicao") && entity.get("eleicao") != null) {
                    mountTable("candidato_eleicoes", (Map) entity.get("eleicao"));
                } else if (fieldName.equals("eleicoesAnteriores") && entity.get("eleicoesAnteriores") != null && !((List) entity.get("eleicoesAnteriores")).isEmpty()) {
                    ((List) entity.get("eleicoesAnteriores")).stream().forEach(eleicoes -> {
                        mountTable("candidato_eleicoes", (Map) eleicoes);
                    });
                } else if (fieldName.equals("emails") && entity.get("emails") != null) {
                    ((List) entity.get("emails")).stream().forEach(email -> {
                        mountTable("candidato_emails", new HashMap() {{
                            put("email", email);
                        }});
                    });
                } else if (fieldName.equals("arquivos") && entity.get("arquivos") != null) {
                    ((List) entity.get("arquivos")).stream().forEach(arquivo -> {
                        mountTable("candidato_arquivos", (Map) arquivo);
                    });
                } else if (fieldName.equals("sites") && entity.get("sites") != null) {
                    ((List) entity.get("sites")).stream().forEach(site -> {
                        mountTable("candidato_sites", new HashMap() {{
                            put("site", site);
                        }});
                    });
                }
            });
        } else {
            tables.put(tableName, createTableWithIdSerial(tableName));
            tables.get(tableName).setColumns(mountBaseOfColumns(entity));
        }
    }
}
