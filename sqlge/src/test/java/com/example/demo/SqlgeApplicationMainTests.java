package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
class SqlgeApplicationMainTests {

    @Test
    void shouldBeStartApplication() {
        SqlgeApplication.main(new String[0]);
    }
}
