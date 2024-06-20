package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Processo} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProcessoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /processos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter npu;

    private LocalDateFilter dataCadastro;

    private StringFilter municipio;

    private StringFilter uf;

    private StringFilter upload;

    private Boolean distinct;

    public ProcessoCriteria() {}

    public ProcessoCriteria(ProcessoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.npu = other.npu == null ? null : other.npu.copy();
        this.dataCadastro = other.dataCadastro == null ? null : other.dataCadastro.copy();
        this.municipio = other.municipio == null ? null : other.municipio.copy();
        this.uf = other.uf == null ? null : other.uf.copy();
        this.upload = other.upload == null ? null : other.upload.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProcessoCriteria copy() {
        return new ProcessoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNpu() {
        return npu;
    }

    public StringFilter npu() {
        if (npu == null) {
            npu = new StringFilter();
        }
        return npu;
    }

    public void setNpu(StringFilter npu) {
        this.npu = npu;
    }

    public LocalDateFilter getDataCadastro() {
        return dataCadastro;
    }

    public LocalDateFilter dataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new LocalDateFilter();
        }
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateFilter dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public StringFilter getMunicipio() {
        return municipio;
    }

    public StringFilter municipio() {
        if (municipio == null) {
            municipio = new StringFilter();
        }
        return municipio;
    }

    public void setMunicipio(StringFilter municipio) {
        this.municipio = municipio;
    }

    public StringFilter getUf() {
        return uf;
    }

    public StringFilter uf() {
        if (uf == null) {
            uf = new StringFilter();
        }
        return uf;
    }

    public void setUf(StringFilter uf) {
        this.uf = uf;
    }

    public StringFilter getUpload() {
        return upload;
    }

    public StringFilter upload() {
        if (upload == null) {
            upload = new StringFilter();
        }
        return upload;
    }

    public void setUpload(StringFilter upload) {
        this.upload = upload;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProcessoCriteria that = (ProcessoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(npu, that.npu) &&
            Objects.equals(dataCadastro, that.dataCadastro) &&
            Objects.equals(municipio, that.municipio) &&
            Objects.equals(uf, that.uf) &&
            Objects.equals(upload, that.upload) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, npu, dataCadastro, municipio, uf, upload, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (npu != null ? "npu=" + npu + ", " : "") +
            (dataCadastro != null ? "dataCadastro=" + dataCadastro + ", " : "") +
            (municipio != null ? "municipio=" + municipio + ", " : "") +
            (uf != null ? "uf=" + uf + ", " : "") +
            (upload != null ? "upload=" + upload + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
