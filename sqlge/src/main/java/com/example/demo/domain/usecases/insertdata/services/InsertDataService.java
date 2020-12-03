package com.example.demo.domain.usecases.insertdata.services;

import com.example.demo.domain.entities.Table;
import com.example.demo.domain.json.services.IParseJsons;
import com.example.demo.repository.GenericInsert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
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
            mountTable("\"candidatos\"", candidate, candidate);
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    void mountTable(final String tableName, final Map entity, final Map candidate) {
        new ArrayList<>(entity.keySet()).forEach(fieldName -> {
            AtomicReference<CompletableFuture> result = new AtomicReference<>(new CompletableFuture());
            if (fieldName.equals("id")) {
                entity.put("fk_partido", ((Map) entity.get("partido")).get("numero"));
                entity.put("fk_substituto", candidate.get("sqeleicao"));
                result.set(genericInsert.insert(dataSource, tableName, entity));
            } else if (fieldName.equals("cargo") && entity.get("cargo") != null) {
                result.set(genericInsert.insert(dataSource, "\"cargos\"", (Map) entity.get("cargo")));
            } else if (fieldName.equals("vices") && entity.get("vices") != null) {
                ((List) entity.get("vices")).stream().forEach(vice -> {
                    Map vicemap = (Map) vice;
                    vicemap.put("fk_candidato", candidate.get("id"));
                    result.set(genericInsert.insert(dataSource, "\"vices\"", vicemap));
                });
            } else if (fieldName.equals("bens") && entity.get("bens") != null) {
                ((List) entity.get("bens")).stream().forEach(bem -> {
                    Map bemMap = (Map) bem;
                    bemMap.put("fk_candidato", candidate.get("id"));
                    result.set(genericInsert.insert(dataSource, "\"candidato_bens\"", bemMap));
                });
            } else if (fieldName.equals("partido") && entity.get("partido") != null) {
                result.set(genericInsert.insert(dataSource, "\"partidos\"", (Map) entity.get("partido")));
            } else if (fieldName.equals("substituto") && entity.get("substituto") != null) {
                result.set(genericInsert.insert(dataSource, "\"candidato_substitutos\"", (Map) entity.get("substituto")));
            } else if (fieldName.equals("eleicao") && entity.get("eleicao") != null) {
                result.set(genericInsert.insert(dataSource, "\"candidato_eleicoes\"", (Map) entity.get("eleicao")));
            } else if (fieldName.equals("eleicoesAnteriores") && entity.get("eleicoesAnteriores") != null && !((List) entity.get("eleicoesAnteriores")).isEmpty()) {
                ((List) entity.get("eleicoesAnteriores")).stream().forEach(eleicoes -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_eleicoes\"", (Map) eleicoes));
                });
            } else if (fieldName.equals("motivos") && entity.get("motivos") != null) {
                ((List) entity.get("motivos")).stream().forEach(motivo -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_motivos\"", new HashMap() {{
                        put("motivo", motivo);
                        put("fk_candidato", candidate.get("id"));
                    }}));
                });
            } else if (fieldName.equals("emails") && entity.get("emails") != null) {
                ((List) entity.get("emails")).stream().forEach(email -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_emails\"", new HashMap() {{
                        put("email", email);
                        put("fk_candidato", candidate.get("id"));
                    }}));
                });
            } else if (fieldName.equals("arquivos") && entity.get("arquivos") != null) {
                ((List) entity.get("arquivos")).stream().forEach(arquivo -> {
                    Map arquivoMap = (Map) arquivo;
                    arquivoMap.put("fk_candidato", candidate.get("id"));
                    result.set(genericInsert.insert(dataSource, "\"candidato_arquivos\"", arquivoMap));
                });
            } else if (fieldName.equals("sites") && entity.get("sites") != null) {
                ((List) entity.get("sites")).stream().forEach(site -> {
                    result.set(genericInsert.insert(dataSource, "\"candidato_sites\"", new HashMap() {{
                        put("site", site);
                        put("fk_candidato", candidate.get("id"));
                    }}));
                });
            }
        });
    }
}
