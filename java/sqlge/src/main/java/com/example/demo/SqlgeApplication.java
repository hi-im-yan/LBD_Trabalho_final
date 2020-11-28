package com.example.demo;

import com.example.demo.domain.usecases.generatetables.factories.GenerateTableFactory;
import com.example.demo.domain.usecases.insertdata.factories.InsertDataFactory;
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

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(SqlgeApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        GenerateTableFactory.generateTablesService().run(dataSource);
        InsertDataFactory.insertDataService().run(dataSource);
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
