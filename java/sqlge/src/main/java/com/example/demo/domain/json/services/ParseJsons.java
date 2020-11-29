package com.example.demo.domain.json.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParseJsons implements IParseJsons {

    private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public Set run() {
        Set<Resource> resources = getAllFiles();

        return resources.stream().map(resource -> {
            try {
                return parseJsonToHashMap(resource.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
    }

    private Set<Resource> getAllFiles() {
        try {
            return new HashSet<>(Arrays.asList(resourcePatternResolver.getResources("/jsons/*.json")));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Map parseJsonToHashMap(InputStream file) {
        try {
            return new ObjectMapper().readValue(file, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
