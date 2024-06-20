package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Municipio;
import com.mycompany.myapp.domain.Uf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IbgeService {

    private final RestTemplate restTemplate;

    @Autowired
    public IbgeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Uf[] getUfs() {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados";
        return restTemplate.getForObject(url, Uf[].class);
    }

    public Municipio[] getMunicipiosByUf(String ufSigla) {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + ufSigla + "/municipios";
        return restTemplate.getForObject(url, Municipio[].class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
