package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
@Table(name = "processo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Processo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{7}-\\d{2}\\.\\d{4}\\.\\d{1}\\.\\d{2}\\.\\d{4}")
    @Column(name = "npu", nullable = false)
    private String npu;

    @NotNull
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @NotNull
    @Column(name = "municipio", nullable = false)
    private String municipio;

    @NotNull
    @Column(name = "uf", nullable = false)
    private String uf;

    @Lob
    @Column(name = "anexo_pdf", nullable = false)
    private byte[] anexoPdf;

    @NotNull
    @Column(name = "anexo_pdf_content_type", nullable = false)
    private String anexoPdfContentType;

    @NotNull
    @Column(name = "upload", nullable = false)
    private String upload;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Processo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNpu() {
        return this.npu;
    }

    public Processo npu(String npu) {
        this.setNpu(npu);
        return this;
    }

    public void setNpu(String npu) {
        this.npu = npu;
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    public Processo dataCadastro(LocalDate dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public Processo municipio(String municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return this.uf;
    }

    public Processo uf(String uf) {
        this.setUf(uf);
        return this;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public byte[] getAnexoPdf() {
        return this.anexoPdf;
    }

    public Processo anexoPdf(byte[] anexoPdf) {
        this.setAnexoPdf(anexoPdf);
        return this;
    }

    public void setAnexoPdf(byte[] anexoPdf) {
        this.anexoPdf = anexoPdf;
    }

    public String getAnexoPdfContentType() {
        return this.anexoPdfContentType;
    }

    public Processo anexoPdfContentType(String anexoPdfContentType) {
        this.anexoPdfContentType = anexoPdfContentType;
        return this;
    }

    public void setAnexoPdfContentType(String anexoPdfContentType) {
        this.anexoPdfContentType = anexoPdfContentType;
    }

    public String getUpload() {
        return this.upload;
    }

    public Processo upload(String upload) {
        this.setUpload(upload);
        return this;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Processo)) {
            return false;
        }
        return id != null && id.equals(((Processo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Processo{" +
            "id=" + getId() +
            ", npu='" + getNpu() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", uf='" + getUf() + "'" +
            ", anexoPdf='" + getAnexoPdf() + "'" +
            ", anexoPdfContentType='" + getAnexoPdfContentType() + "'" +
            ", upload='" + getUpload() + "'" +
            "}";
    }
}
