package com.example.demo.domain.usecases.insertdata.factories;

import com.example.demo.domain.json.services.ParseJsons;
import com.example.demo.domain.usecases.insertdata.services.IInsertDataService;
import com.example.demo.domain.usecases.insertdata.services.InsertDataService;
import com.example.demo.repository.GenericInsert;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class InsertDataFactory {

    public static IInsertDataService insertDataService() {
        return new InsertDataService(new GenericInsert(), new ParseJsons());
    }
}
