package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Processo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessoDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{7}-\\d{2}\\.\\d{4}\\.\\d{1}\\.\\d{2}\\.\\d{4}")
    private String npu;

    @NotNull
    private LocalDate dataCadastro;

    @NotNull
    private String municipio;

    @NotNull
    private String uf;

    @Lob
    private byte[] anexoPdf;

    private String anexoPdfContentType;

    @NotNull
    private String upload;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNpu() {
        return npu;
    }

    public void setNpu(String npu) {
        this.npu = npu;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public byte[] getAnexoPdf() {
        return anexoPdf;
    }

    public void setAnexoPdf(byte[] anexoPdf) {
        this.anexoPdf = anexoPdf;
    }

    public String getAnexoPdfContentType() {
        return anexoPdfContentType;
    }

    public void setAnexoPdfContentType(String anexoPdfContentType) {
        this.anexoPdfContentType = anexoPdfContentType;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessoDTO)) {
            return false;
        }

        ProcessoDTO processoDTO = (ProcessoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessoDTO{" +
            "id=" + getId() +
            ", npu='" + getNpu() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", uf='" + getUf() + "'" +
            ", anexoPdf='" + getAnexoPdf() + "'" +
            ", upload='" + getUpload() + "'" +
            "}";
    }
}
