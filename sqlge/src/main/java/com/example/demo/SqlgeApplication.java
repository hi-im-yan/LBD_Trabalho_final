package com.example.demo;

import com.example.demo.domain.usecases.generatetables.factories.GenerateTableFactory;
import com.example.demo.domain.usecases.generatetables.services.GenerateTablesService;
import com.example.demo.domain.usecases.insertdata.factories.InsertDataFactory;
import com.example.demo.repository.GenericExecute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class SqlgeApplication implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(SqlgeApplication.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private GenericExecute genericExecute;

    private static final String DROP_SCHEMAS_QUERY = "DROP SCHEMA public CASCADE;\n" +
            "            CREATE SCHEMA public;\n" +
            "            GRANT ALL ON SCHEMA public TO root;\n" +
            "            GRANT ALL ON SCHEMA public TO public;";

    private static final String ALTER_IDS_TO_BIGINT_QUERY = "" +
            "ALTER TABLE public.candidato_arquivos ALTER COLUMN idarquivo TYPE BIGINT USING idarquivo::bigint;" +
            "ALTER TABLE public.candidatos ALTER COLUMN id TYPE BIGINT USING id::bigint;" +
            "ALTER TABLE public.candidato_eleicoes ALTER COLUMN id TYPE BIGINT USING id::bigint;" +
            "ALTER TABLE public.partidos ALTER COLUMN numero TYPE BIGINT USING numero::bigint;" +
            "ALTER TABLE public.candidato_substitutos ALTER COLUMN sqeleicao TYPE BIGINT USING sqeleicao::bigint;";

    private static final String REMOVE_DP_IDS_QUERY = "delete from public.candidato_arquivos where public.candidato_arquivos.idarquivo in (select public.candidato_arquivos.idarquivo from public.candidato_arquivos GROUP BY public.candidato_arquivos.idarquivo HAVING count(*) > 1);\n" +
            "delete from public.candidatos where public.candidatos.id in (select public.candidatos.id from public.candidatos GROUP BY public.candidatos.id HAVING count(*) > 1);\n" +
            "delete from public.candidato_eleicoes where public.candidato_eleicoes.id in (select public.candidato_eleicoes.id from public.candidato_eleicoes GROUP BY public.candidato_eleicoes.id HAVING count(*) > 1);\n" +
            "delete from public.partidos where public.partidos.numero in (select public.partidos.numero from public.partidos GROUP BY public.partidos.numero HAVING count(*) > 1);\n" +
            "delete from public.candidato_substitutos where public.candidato_substitutos.sqeleicao in (select public.candidato_substitutos.sqeleicao from public.candidato_substitutos GROUP BY public.candidato_substitutos.sqeleicao HAVING count(*) > 1);";

    private static final String CREATE_PKS_QUERY = "ALTER TABLE public.candidato_arquivos ADD CONSTRAINT arquivos_pkey PRIMARY KEY (idarquivo);\n" +
            "ALTER TABLE public.candidatos ADD CONSTRAINT candidato_pkey PRIMARY KEY (id);\n" +
            "ALTER TABLE public.candidato_eleicoes ADD CONSTRAINT eleicao_pkey PRIMARY KEY (id);\n" +
            "ALTER TABLE public.partidos ADD CONSTRAINT partido_pkey PRIMARY KEY (numero);\n" +
            "ALTER TABLE public.candidato_substitutos ADD CONSTRAINT substituto_pkey PRIMARY KEY (sqeleicao);";

    private static final String UPDATE_PKS_TO_MINUS_ONE_WHERE_IS_NULL_QUERY = "UPDATE public.candidato_arquivos SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';\n" +
            "UPDATE public.candidato_bens SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';\n" +
            "UPDATE public.candidato_emails SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';\n" +
            "UPDATE public.candidato_motivos SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';\n" +
            "UPDATE public.candidatos SET fk_substituto = '-1' WHERE fk_substituto is NULL OR fk_substituto LIKE 'null';\n" +
            "UPDATE public.candidato_sites SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';\n" +
            "UPDATE public.vices SET fk_candidato = '-1' WHERE fk_candidato is NULL OR fk_candidato LIKE 'null';";

    private static final String CHANGE_PKS_TO_BIG_INT_QUERY = "ALTER TABLE public.candidato_arquivos ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;\n" +
            "ALTER TABLE public.candidato_bens ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;\n" +
            "ALTER TABLE public.candidato_emails ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;\n" +
            "ALTER TABLE public.candidato_motivos ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;\n" +
            "ALTER TABLE public.candidatos ALTER COLUMN fk_substituto TYPE BIGINT USING fk_substituto::bigint;\n" +
            "ALTER TABLE public.candidato_sites ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;\n" +
            "ALTER TABLE public.vices ALTER COLUMN fk_candidato TYPE BIGINT USING fk_candidato::bigint;";

    private static final String CHANGE_PKS_TO_NULL_WHERE_IS_LESS_THAN_ZERO_QUERY = "UPDATE public.candidato_arquivos SET fk_candidato = null WHERE fk_candidato < 0;\n" +
            "UPDATE public.candidato_bens SET fk_candidato = null WHERE fk_candidato < 0;\n" +
            "UPDATE public.candidato_emails SET fk_candidato = null WHERE fk_candidato < 0;\n" +
            "UPDATE public.candidato_motivos SET fk_candidato = null WHERE fk_candidato < 0;\n" +
            "UPDATE public.candidatos SET fk_substituto = null WHERE fk_substituto < 0;\n" +
            "UPDATE public.candidato_sites SET fk_candidato = null WHERE fk_candidato < 0;\n" +
            "UPDATE public.vices SET fk_candidato = null WHERE fk_candidato < 0;";

    private static final String CREATE_FKS_FIELDS_QUERY = "ALTER TABLE public.candidato_arquivos ADD fk_candidato VARCHAR(512);\n" +
            "ALTER TABLE public.candidato_bens ADD fk_candidato VARCHAR(512);\n" +
            "ALTER TABLE public.candidato_emails ADD fk_candidato VARCHAR(512);\n" +
            "ALTER TABLE public.candidato_motivos ADD fk_candidato VARCHAR(512);\n" +
            "ALTER TABLE public.candidatos ADD fk_substituto VARCHAR(512);\n" +
            "ALTER TABLE public.candidato_sites ADD fk_candidato VARCHAR(512);\n" +
            "ALTER TABLE public.vices ADD fk_candidato VARCHAR(512);";

    private static final String ADD_FKS_QUERY = "ALTER TABLE public.candidato_arquivos ADD CONSTRAINT fk_arquivos_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);\n" +
            "ALTER TABLE public.candidato_bens ADD CONSTRAINT fk_bens_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);\n" +
            "ALTER TABLE public.candidato_emails ADD CONSTRAINT fk_email_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);\n" +
            "ALTER TABLE public.candidato_motivos ADD CONSTRAINT fk_motivos_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);\n" +
            "ALTER TABLE public.candidatos ADD CONSTRAINT fk_motivos_substituto FOREIGN KEY (fk_substituto) REFERENCES public.candidato_substitutos(sqeleicao);\n" +
            "ALTER TABLE public.candidato_sites ADD CONSTRAINT fk_sites_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);\n" +
            "ALTER TABLE public.vices ADD CONSTRAINT fk_vices_candidato FOREIGN KEY (fk_candidato) REFERENCES public.candidatos(id);";

    public static void main(String[] args) {
        SpringApplication.run(SqlgeApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        logger.info("INIT DROP_SCHEMAS_QUERY");
        genericExecute.run(dataSource, DROP_SCHEMAS_QUERY);
        logger.info("INIT CREATE_TABLES");
        GenerateTableFactory.generateTablesService().run(dataSource);
        logger.info("INIT CREATE_FKS_FIELDS_QUERY");
        genericExecute.run(dataSource, CREATE_FKS_FIELDS_QUERY);
        logger.info("INIT INSERT_DATA");
        InsertDataFactory.insertDataService().run(dataSource);
        logger.info("INIT UPDATE_PKS_TO_MINUS_ONE_WHERE_IS_NULL_QUERY");
        genericExecute.run(dataSource, UPDATE_PKS_TO_MINUS_ONE_WHERE_IS_NULL_QUERY);
        logger.info("INIT CHANGE_PKS_TO_BIG_INT_QUERY");
        genericExecute.run(dataSource, CHANGE_PKS_TO_BIG_INT_QUERY);
        logger.info("INIT CHANGE_PKS_TO_NULL_WHERE_IS_LESS_THAN_ZERO_QUERY");
        genericExecute.run(dataSource, CHANGE_PKS_TO_NULL_WHERE_IS_LESS_THAN_ZERO_QUERY);
        logger.info("INIT ALTER_IDS_TO_BIGINT_QUERY");
        genericExecute.run(dataSource, ALTER_IDS_TO_BIGINT_QUERY);
        logger.info("INIT REMOVE_DP_IDS_QUERY");
        genericExecute.run(dataSource, REMOVE_DP_IDS_QUERY);
        logger.info("INIT CREATE_PKS_QUERY");
        genericExecute.run(dataSource, CREATE_PKS_QUERY);
        logger.info("INIT ADD_FKS_QUERY");
        genericExecute.run(dataSource, ADD_FKS_QUERY);
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(1000);
        executor.initialize();
        return executor;
    }
}
