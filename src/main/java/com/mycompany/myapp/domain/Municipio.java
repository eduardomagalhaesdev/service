package com.mycompany.myapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Municipio {

    private String id;
    private String nome;
    private Microrregiao microrregiao;
}
