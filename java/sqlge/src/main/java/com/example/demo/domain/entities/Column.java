package com.example.demo.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Column {

    private String name;

    private String type;

    private String length;

    private String precision;

    private String scale;

    private Boolean notnull;

    private String defaultValue;
}
