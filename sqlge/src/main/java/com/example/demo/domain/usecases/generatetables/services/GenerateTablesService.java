package com.example.demo.domain.usecases.generatetables.services;

import com.example.demo.domain.entities.Table;
import com.example.demo.domain.json.services.IParseJsons;
import com.example.demo.repository.GenericExecute;
import com.example.demo.utils.GenerateSqlTables;
import com.example.demo.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerateTablesService implements IGenerateTablesService {

    private static final Map<String, Table> tables = new HashMap<String, Table>();
    private final GenericExecute genericExecute;
    private final IParseJsons parseJsons;
    Logger logger = LoggerFactory.getLogger(GenerateTablesService.class);

    @Transactional(propagation = Propagation.SUPPORTS)
    public void run(DataSource dataSource) {
        Set<Map<String, Object>> candidates = parseJsons.run();
        logger.info("init mount tables");
        candidates.stream().forEach(candidate -> mountTable("candidatos", candidate));
        logger.info("finished mount tables");
        genericExecute.run(dataSource, GenerateSqlTables.run(tables));
    }

    private Table createTableWithIdSerial(final String name) {
        return Table.builder().name(name).columns(new HashMap<>()).build();
    }

    private Map<String, Object> mountBaseOfColumns(Map<String, Object> entity) {
        Map<String, Object> columns = new HashMap<>();
        new ArrayList<>(entity.keySet()).forEach(fieldName -> {
            if (Boolean.FALSE.equals(columnIsATableOrFromOtherTable(entity, fieldName))) {
                columns.put(fieldName, "");
            }
        });

        return columns;
    }

    private Boolean columnIsATableOrFromOtherTable(Map<String, Object> entity, String fieldName) {
        return Boolean.TRUE.equals(isColumnIsATable((Optional.ofNullable(entity.get(fieldName)).orElse(new Object()).getClass().getSimpleName())))
                || Boolean.TRUE.equals(isColumnIsATable(fieldName));
    }

    private Boolean isColumnIsATable(String fieldType) {
        return StringUtils.containWords(fieldType, new String[]{"List", "Set", "Map", "Tree", "Queue", "motivos", "vices"});
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    void mountTable(final String tableName, Map<String, Object> entity) {
        if (tables.containsKey(tableName) && tables.get(tableName) != null) {
            new ArrayList<>(entity.keySet()).forEach(fieldName -> {
                if (Boolean.FALSE.equals(columnIsATableOrFromOtherTable(entity, fieldName))) {
                    if (!tables.get(tableName).getColumns().containsKey(fieldName)) {
                        tables.get(tableName).getColumns().put(fieldName, "");
                    }
                } else if (fieldName.equals("cargo") && entity.get("cargo") != null) {
                    mountTable("cargos", (Map) entity.get("cargo"));
                } else if (fieldName.equals("vices") && entity.get("vices") != null) {
                    ((List) entity.get("vices")).stream().forEach(vice -> {
                        mountTable("vices", (Map) vice);
                    });
                } else if (fieldName.equals("motivos") && entity.get("motivos") != null) {
                    ((List) entity.get("motivos")).stream().forEach(motivo -> {
                        mountTable("candidato_motivos", new HashMap<String, Object>() {{
                            put("motivo", motivo);
                        }});
                    });
                } else if (fieldName.equals("bens") && entity.get("bens") != null) {
                    ((List) entity.get("bens")).stream().forEach(bem -> {
                        mountTable("candidato_bens", (Map) bem);
                    });
                } else if (fieldName.equals("partido") && entity.get("partido") != null) {
                    mountTable("partidos", (Map) entity.get("partido"));
                } else if (fieldName.equals("eleicao") && entity.get("eleicao") != null) {
                    mountTable("candidato_eleicoes", (Map) entity.get("eleicao"));
                } else if (fieldName.equals("substituto") && entity.get("substituto") != null) {
                    mountTable("candidato_substitutos", (Map) entity.get("substituto"));
                } else if (fieldName.equals("eleicoesAnteriores") && entity.get("eleicoesAnteriores") != null && !((List) entity.get("eleicoesAnteriores")).isEmpty()) {
                    ((List) entity.get("eleicoesAnteriores")).stream().forEach(eleicoes -> {
                        mountTable("candidato_eleicoes", (Map) eleicoes);
                    });
                } else if (fieldName.equals("emails") && entity.get("emails") != null) {
                    ((List) entity.get("emails")).stream().forEach(email -> {
                        mountTable("candidato_emails", new HashMap<String, Object>() {{
                            put("email", email);
                        }});
                    });
                } else if (fieldName.equals("arquivos") && entity.get("arquivos") != null) {
                    ((List) entity.get("arquivos")).stream().forEach(arquivo -> {
                        mountTable("candidato_arquivos", (Map) arquivo);
                    });
                } else if (fieldName.equals("sites") && entity.get("sites") != null) {
                    ((List) entity.get("sites")).stream().forEach(site -> {
                        mountTable("candidato_sites", new HashMap<String, Object>() {{
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
