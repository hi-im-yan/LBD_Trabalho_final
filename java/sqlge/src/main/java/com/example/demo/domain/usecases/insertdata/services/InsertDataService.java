package com.example.demo.domain.usecases.insertdata.services;

import com.example.demo.domain.entities.Table;
import com.example.demo.domain.json.services.IParseJsons;
import com.example.demo.repository.GenericInsert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class InsertDataService implements IInsertDataService {

    private static final Map<String, Table> tables = new HashMap();
    private final GenericInsert genericInsert;
    private final IParseJsons parseJsons;
    DataSource dataSource = null;

    public void run(DataSource dataSource) {
        this.dataSource = dataSource;
        Set<Map<String, Object>> candidates = parseJsons.run();
        candidates.forEach(candidate -> {
            mountTable("\"candidatos\"", candidate);
        });
    }

    private void mountTable(final String tableName, final Map entity) {
        entity.keySet().parallelStream().forEach(fieldName -> {
            AtomicReference<CompletableFuture> result = new AtomicReference<>(new CompletableFuture());
            if (fieldName.equals("id")) {
                result.set(genericInsert.insert(dataSource, tableName, entity));
            } else if (fieldName.equals("cargo") && entity.get("cargo") != null) {
                result.set(genericInsert.insert(dataSource, "\"cargos\"", (Map) entity.get("cargo")));
            } else if (fieldName.equals("vices") && entity.get("vices") != null) {
                ((List) entity.get("vices")).stream().forEach(vice -> {
                    result.set(genericInsert.insert(dataSource, "\"vices\"", (Map) vice));
                });
            } else if (fieldName.equals("bens") && entity.get("bens") != null) {
                ((List) entity.get("bens")).stream().forEach(bem -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_bens\"", (Map) bem));
                });
            } else if (fieldName.equals("partido") && entity.get("partido") != null) {
                result.set(genericInsert.insert(dataSource, "\"partidos\"", (Map) entity.get("partido")));
            } else if (fieldName.equals("eleicao") && entity.get("eleicao") != null) {
                result.set(genericInsert.insert(dataSource, "\"candidato_eleicoes\"", (Map) entity.get("eleicao")));
            } else if (fieldName.equals("eleicoesAnteriores") && entity.get("eleicoesAnteriores") != null && !((List) entity.get("eleicoesAnteriores")).isEmpty()) {
                ((List) entity.get("eleicoesAnteriores")).stream().forEach(eleicoes -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_eleicoes\"", (Map) eleicoes));
                });
            } else if (fieldName.equals("emails") && entity.get("emails") != null) {
                ((List) entity.get("emails")).stream().forEach(email -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_emails\"", new HashMap<>() {{
                        put("email", email);
                    }}));
                });
            } else if (fieldName.equals("arquivos") && entity.get("arquivos") != null) {
                ((List) entity.get("arquivos")).stream().forEach(arquivo -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_arquivos\"", (Map) arquivo));
                });
            } else if (fieldName.equals("sites") && entity.get("sites") != null) {
                ((List) entity.get("sites")).stream().forEach(site -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_sites\"", new HashMap<>() {{
                        put("site", site);
                    }}));
                });
            }
        });
    }
}
