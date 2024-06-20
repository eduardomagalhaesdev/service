package com.mycompany.myapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Microrregiao {

    private String id;
    private String nome;
    private Mesorregiao mesorregiao;
}
