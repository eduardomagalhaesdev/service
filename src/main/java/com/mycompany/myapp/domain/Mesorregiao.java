package com.mycompany.myapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mesorregiao {

    private String id;
    private String nome;
    private Uf uf;
}
