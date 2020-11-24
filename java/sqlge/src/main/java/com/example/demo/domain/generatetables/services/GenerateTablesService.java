package com.example.demo.domain.generatetables.services;

import com.example.demo.domain.json.services.IParseJsons;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateTablesService implements IGenerateTablesService {

    private final IParseJsons parseJsons;

    public void run() {}
}
