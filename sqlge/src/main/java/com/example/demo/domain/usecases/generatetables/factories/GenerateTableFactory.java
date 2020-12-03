package com.example.demo.domain.usecases.generatetables.factories;

import com.example.demo.domain.json.services.ParseJsons;
import com.example.demo.domain.usecases.generatetables.services.GenerateTablesService;
import com.example.demo.domain.usecases.generatetables.services.IGenerateTablesService;
import com.example.demo.repository.GenericExecute;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GenerateTableFactory {

    public static IGenerateTablesService generateTablesService() {
        return new GenerateTablesService(new GenericExecute(), new ParseJsons());
    }
}
