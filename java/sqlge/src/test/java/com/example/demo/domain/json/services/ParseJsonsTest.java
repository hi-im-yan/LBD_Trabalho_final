package com.example.demo.domain.json.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith({SpringExtension.class})
class ParseJsonsTest {

    @InjectMocks
    private ParseJsons parseJsons;

    @Test
    void shouldReturn8607Maps() {
        assertEquals(8607, parseJsons.run().size());
    }
}