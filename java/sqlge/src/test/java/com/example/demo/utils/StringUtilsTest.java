package com.example.demo.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
class StringUtilsTest {

    @Test
    void shouldReturnTrueWhenFindWord() {
        Assert.assertTrue(StringUtils.containWords("Map", new String[]{"Set", "Array", "Map"}));
    }

    @Test
    void shouldReturnFalseWhenNotFindWord() {
        Assert.assertFalse(StringUtils.containWords("Map", new String[]{"Set", "Array"}));
    }
}