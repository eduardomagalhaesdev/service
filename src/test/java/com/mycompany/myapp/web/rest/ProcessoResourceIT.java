package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Processo;
import com.mycompany.myapp.repository.ProcessoRepository;
import com.mycompany.myapp.service.criteria.ProcessoCriteria;
import com.mycompany.myapp.service.dto.ProcessoDTO;
import com.mycompany.myapp.service.mapper.ProcessoMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProcessoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessoResourceIT {

    private static final String DEFAULT_NPU = "\\ddddddd-\\dd\\ \\dddd\\c\\d\\s\\dd\\D\\dddd";
    private static final String UPDATED_NPU = "\\ddddddd-\\dd\\,\\dddd\\v\\d\\[\\dd\\X\\dddd";

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_CADASTRO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_UF = "AAAAAAAAAA";
    private static final String UPDATED_UF = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ANEXO_PDF = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANEXO_PDF = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANEXO_PDF_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANEXO_PDF_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_UPLOAD = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProcessoMapper processoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessoMockMvc;

    private Processo processo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createEntity(EntityManager em) {
        Processo processo = new Processo()
            .npu(DEFAULT_NPU)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .municipio(DEFAULT_MUNICIPIO)
            .uf(DEFAULT_UF)
            .anexoPdf(DEFAULT_ANEXO_PDF)
            .anexoPdfContentType(DEFAULT_ANEXO_PDF_CONTENT_TYPE)
            .upload(DEFAULT_UPLOAD);
        return processo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createUpdatedEntity(EntityManager em) {
        Processo processo = new Processo()
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .anexoPdf(UPDATED_ANEXO_PDF)
            .anexoPdfContentType(UPDATED_ANEXO_PDF_CONTENT_TYPE)
            .upload(UPDATED_UPLOAD);
        return processo;
    }

    @BeforeEach
    public void initTest() {
        processo = createEntity(em);
    }

    @Test
    @Transactional
    void createProcesso() throws Exception {
        int databaseSizeBeforeCreate = processoRepository.findAll().size();
        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);
        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeCreate + 1);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getNpu()).isEqualTo(DEFAULT_NPU);
        assertThat(testProcesso.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testProcesso.getMunicipio()).isEqualTo(DEFAULT_MUNICIPIO);
        assertThat(testProcesso.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testProcesso.getAnexoPdf()).isEqualTo(DEFAULT_ANEXO_PDF);
        assertThat(testProcesso.getAnexoPdfContentType()).isEqualTo(DEFAULT_ANEXO_PDF_CONTENT_TYPE);
        assertThat(testProcesso.getUpload()).isEqualTo(DEFAULT_UPLOAD);
    }

    @Test
    @Transactional
    void createProcessoWithExistingId() throws Exception {
        // Create the Processo with an existing ID
        processo.setId(1L);
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        int databaseSizeBeforeCreate = processoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNpuIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setNpu(null);

        // Create the Processo, which fails.
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setDataCadastro(null);

        // Create the Processo, which fails.
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMunicipioIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setMunicipio(null);

        // Create the Processo, which fails.
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUfIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setUf(null);

        // Create the Processo, which fails.
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUploadIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setUpload(null);

        // Create the Processo, which fails.
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        restProcessoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcessos() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processo.getId().intValue())))
            .andExpect(jsonPath("$.[*].npu").value(hasItem(DEFAULT_NPU)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].anexoPdfContentType").value(hasItem(DEFAULT_ANEXO_PDF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anexoPdf").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO_PDF))))
            .andExpect(jsonPath("$.[*].upload").value(hasItem(DEFAULT_UPLOAD)));
    }

    @Test
    @Transactional
    void getProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get the processo
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL_ID, processo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processo.getId().intValue()))
            .andExpect(jsonPath("$.npu").value(DEFAULT_NPU))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()))
            .andExpect(jsonPath("$.municipio").value(DEFAULT_MUNICIPIO))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF))
            .andExpect(jsonPath("$.anexoPdfContentType").value(DEFAULT_ANEXO_PDF_CONTENT_TYPE))
            .andExpect(jsonPath("$.anexoPdf").value(Base64Utils.encodeToString(DEFAULT_ANEXO_PDF)))
            .andExpect(jsonPath("$.upload").value(DEFAULT_UPLOAD));
    }

    @Test
    @Transactional
    void getProcessosByIdFiltering() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        Long id = processo.getId();

        defaultProcessoShouldBeFound("id.equals=" + id);
        defaultProcessoShouldNotBeFound("id.notEquals=" + id);

        defaultProcessoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProcessoShouldNotBeFound("id.greaterThan=" + id);

        defaultProcessoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProcessoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProcessosByNpuIsEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where npu equals to DEFAULT_NPU
        defaultProcessoShouldBeFound("npu.equals=" + DEFAULT_NPU);

        // Get all the processoList where npu equals to UPDATED_NPU
        defaultProcessoShouldNotBeFound("npu.equals=" + UPDATED_NPU);
    }

    @Test
    @Transactional
    void getAllProcessosByNpuIsInShouldWork() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where npu in DEFAULT_NPU or UPDATED_NPU
        defaultProcessoShouldBeFound("npu.in=" + DEFAULT_NPU + "," + UPDATED_NPU);

        // Get all the processoList where npu equals to UPDATED_NPU
        defaultProcessoShouldNotBeFound("npu.in=" + UPDATED_NPU);
    }

    @Test
    @Transactional
    void getAllProcessosByNpuIsNullOrNotNull() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where npu is not null
        defaultProcessoShouldBeFound("npu.specified=true");

        // Get all the processoList where npu is null
        defaultProcessoShouldNotBeFound("npu.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessosByNpuContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where npu contains DEFAULT_NPU
        defaultProcessoShouldBeFound("npu.contains=" + DEFAULT_NPU);

        // Get all the processoList where npu contains UPDATED_NPU
        defaultProcessoShouldNotBeFound("npu.contains=" + UPDATED_NPU);
    }

    @Test
    @Transactional
    void getAllProcessosByNpuNotContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where npu does not contain DEFAULT_NPU
        defaultProcessoShouldNotBeFound("npu.doesNotContain=" + DEFAULT_NPU);

        // Get all the processoList where npu does not contain UPDATED_NPU
        defaultProcessoShouldBeFound("npu.doesNotContain=" + UPDATED_NPU);
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro equals to DEFAULT_DATA_CADASTRO
        defaultProcessoShouldBeFound("dataCadastro.equals=" + DEFAULT_DATA_CADASTRO);

        // Get all the processoList where dataCadastro equals to UPDATED_DATA_CADASTRO
        defaultProcessoShouldNotBeFound("dataCadastro.equals=" + UPDATED_DATA_CADASTRO);
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsInShouldWork() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro in DEFAULT_DATA_CADASTRO or UPDATED_DATA_CADASTRO
        defaultProcessoShouldBeFound("dataCadastro.in=" + DEFAULT_DATA_CADASTRO + "," + UPDATED_DATA_CADASTRO);

        // Get all the processoList where dataCadastro equals to UPDATED_DATA_CADASTRO
        defaultProcessoShouldNotBeFound("dataCadastro.in=" + UPDATED_DATA_CADASTRO);
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsNullOrNotNull() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro is not null
        defaultProcessoShouldBeFound("dataCadastro.specified=true");

        // Get all the processoList where dataCadastro is null
        defaultProcessoShouldNotBeFound("dataCadastro.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro is greater than or equal to DEFAULT_DATA_CADASTRO
        defaultProcessoShouldBeFound("dataCadastro.greaterThanOrEqual=" + DEFAULT_DATA_CADASTRO);

        // Get all the processoList where dataCadastro is greater than or equal to UPDATED_DATA_CADASTRO
        defaultProcessoShouldNotBeFound("dataCadastro.greaterThanOrEqual=" + UPDATED_DATA_CADASTRO);
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro is less than or equal to DEFAULT_DATA_CADASTRO
        defaultProcessoShouldBeFound("dataCadastro.lessThanOrEqual=" + DEFAULT_DATA_CADASTRO);

        // Get all the processoList where dataCadastro is less than or equal to SMALLER_DATA_CADASTRO
        defaultProcessoShouldNotBeFound("dataCadastro.lessThanOrEqual=" + SMALLER_DATA_CADASTRO);
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsLessThanSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro is less than DEFAULT_DATA_CADASTRO
        defaultProcessoShouldNotBeFound("dataCadastro.lessThan=" + DEFAULT_DATA_CADASTRO);

        // Get all the processoList where dataCadastro is less than UPDATED_DATA_CADASTRO
        defaultProcessoShouldBeFound("dataCadastro.lessThan=" + UPDATED_DATA_CADASTRO);
    }

    @Test
    @Transactional
    void getAllProcessosByDataCadastroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where dataCadastro is greater than DEFAULT_DATA_CADASTRO
        defaultProcessoShouldNotBeFound("dataCadastro.greaterThan=" + DEFAULT_DATA_CADASTRO);

        // Get all the processoList where dataCadastro is greater than SMALLER_DATA_CADASTRO
        defaultProcessoShouldBeFound("dataCadastro.greaterThan=" + SMALLER_DATA_CADASTRO);
    }

    @Test
    @Transactional
    void getAllProcessosByMunicipioIsEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where municipio equals to DEFAULT_MUNICIPIO
        defaultProcessoShouldBeFound("municipio.equals=" + DEFAULT_MUNICIPIO);

        // Get all the processoList where municipio equals to UPDATED_MUNICIPIO
        defaultProcessoShouldNotBeFound("municipio.equals=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllProcessosByMunicipioIsInShouldWork() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where municipio in DEFAULT_MUNICIPIO or UPDATED_MUNICIPIO
        defaultProcessoShouldBeFound("municipio.in=" + DEFAULT_MUNICIPIO + "," + UPDATED_MUNICIPIO);

        // Get all the processoList where municipio equals to UPDATED_MUNICIPIO
        defaultProcessoShouldNotBeFound("municipio.in=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllProcessosByMunicipioIsNullOrNotNull() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where municipio is not null
        defaultProcessoShouldBeFound("municipio.specified=true");

        // Get all the processoList where municipio is null
        defaultProcessoShouldNotBeFound("municipio.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessosByMunicipioContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where municipio contains DEFAULT_MUNICIPIO
        defaultProcessoShouldBeFound("municipio.contains=" + DEFAULT_MUNICIPIO);

        // Get all the processoList where municipio contains UPDATED_MUNICIPIO
        defaultProcessoShouldNotBeFound("municipio.contains=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllProcessosByMunicipioNotContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where municipio does not contain DEFAULT_MUNICIPIO
        defaultProcessoShouldNotBeFound("municipio.doesNotContain=" + DEFAULT_MUNICIPIO);

        // Get all the processoList where municipio does not contain UPDATED_MUNICIPIO
        defaultProcessoShouldBeFound("municipio.doesNotContain=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllProcessosByUfIsEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where uf equals to DEFAULT_UF
        defaultProcessoShouldBeFound("uf.equals=" + DEFAULT_UF);

        // Get all the processoList where uf equals to UPDATED_UF
        defaultProcessoShouldNotBeFound("uf.equals=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllProcessosByUfIsInShouldWork() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where uf in DEFAULT_UF or UPDATED_UF
        defaultProcessoShouldBeFound("uf.in=" + DEFAULT_UF + "," + UPDATED_UF);

        // Get all the processoList where uf equals to UPDATED_UF
        defaultProcessoShouldNotBeFound("uf.in=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllProcessosByUfIsNullOrNotNull() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where uf is not null
        defaultProcessoShouldBeFound("uf.specified=true");

        // Get all the processoList where uf is null
        defaultProcessoShouldNotBeFound("uf.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessosByUfContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where uf contains DEFAULT_UF
        defaultProcessoShouldBeFound("uf.contains=" + DEFAULT_UF);

        // Get all the processoList where uf contains UPDATED_UF
        defaultProcessoShouldNotBeFound("uf.contains=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllProcessosByUfNotContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where uf does not contain DEFAULT_UF
        defaultProcessoShouldNotBeFound("uf.doesNotContain=" + DEFAULT_UF);

        // Get all the processoList where uf does not contain UPDATED_UF
        defaultProcessoShouldBeFound("uf.doesNotContain=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllProcessosByUploadIsEqualToSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where upload equals to DEFAULT_UPLOAD
        defaultProcessoShouldBeFound("upload.equals=" + DEFAULT_UPLOAD);

        // Get all the processoList where upload equals to UPDATED_UPLOAD
        defaultProcessoShouldNotBeFound("upload.equals=" + UPDATED_UPLOAD);
    }

    @Test
    @Transactional
    void getAllProcessosByUploadIsInShouldWork() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where upload in DEFAULT_UPLOAD or UPDATED_UPLOAD
        defaultProcessoShouldBeFound("upload.in=" + DEFAULT_UPLOAD + "," + UPDATED_UPLOAD);

        // Get all the processoList where upload equals to UPDATED_UPLOAD
        defaultProcessoShouldNotBeFound("upload.in=" + UPDATED_UPLOAD);
    }

    @Test
    @Transactional
    void getAllProcessosByUploadIsNullOrNotNull() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where upload is not null
        defaultProcessoShouldBeFound("upload.specified=true");

        // Get all the processoList where upload is null
        defaultProcessoShouldNotBeFound("upload.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessosByUploadContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where upload contains DEFAULT_UPLOAD
        defaultProcessoShouldBeFound("upload.contains=" + DEFAULT_UPLOAD);

        // Get all the processoList where upload contains UPDATED_UPLOAD
        defaultProcessoShouldNotBeFound("upload.contains=" + UPDATED_UPLOAD);
    }

    @Test
    @Transactional
    void getAllProcessosByUploadNotContainsSomething() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList where upload does not contain DEFAULT_UPLOAD
        defaultProcessoShouldNotBeFound("upload.doesNotContain=" + DEFAULT_UPLOAD);

        // Get all the processoList where upload does not contain UPDATED_UPLOAD
        defaultProcessoShouldBeFound("upload.doesNotContain=" + UPDATED_UPLOAD);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProcessoShouldBeFound(String filter) throws Exception {
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processo.getId().intValue())))
            .andExpect(jsonPath("$.[*].npu").value(hasItem(DEFAULT_NPU)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].anexoPdfContentType").value(hasItem(DEFAULT_ANEXO_PDF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anexoPdf").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO_PDF))))
            .andExpect(jsonPath("$.[*].upload").value(hasItem(DEFAULT_UPLOAD)));

        // Check, that the count call also returns 1
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProcessoShouldNotBeFound(String filter) throws Exception {
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProcesso() throws Exception {
        // Get the processo
        restProcessoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo
        Processo updatedProcesso = processoRepository.findById(processo.getId()).get();
        // Disconnect from session so that the updates on updatedProcesso are not directly saved in db
        em.detach(updatedProcesso);
        updatedProcesso
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .anexoPdf(UPDATED_ANEXO_PDF)
            .anexoPdfContentType(UPDATED_ANEXO_PDF_CONTENT_TYPE)
            .upload(UPDATED_UPLOAD);
        ProcessoDTO processoDTO = processoMapper.toDto(updatedProcesso);

        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getNpu()).isEqualTo(UPDATED_NPU);
        assertThat(testProcesso.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testProcesso.getMunicipio()).isEqualTo(UPDATED_MUNICIPIO);
        assertThat(testProcesso.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testProcesso.getAnexoPdf()).isEqualTo(UPDATED_ANEXO_PDF);
        assertThat(testProcesso.getAnexoPdfContentType()).isEqualTo(UPDATED_ANEXO_PDF_CONTENT_TYPE);
        assertThat(testProcesso.getUpload()).isEqualTo(UPDATED_UPLOAD);
    }

    @Test
    @Transactional
    void putNonExistingProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessoWithPatch() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo using partial update
        Processo partialUpdatedProcesso = new Processo();
        partialUpdatedProcesso.setId(processo.getId());

        partialUpdatedProcesso
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .anexoPdf(UPDATED_ANEXO_PDF)
            .anexoPdfContentType(UPDATED_ANEXO_PDF_CONTENT_TYPE)
            .upload(UPDATED_UPLOAD);

        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getNpu()).isEqualTo(UPDATED_NPU);
        assertThat(testProcesso.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testProcesso.getMunicipio()).isEqualTo(DEFAULT_MUNICIPIO);
        assertThat(testProcesso.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testProcesso.getAnexoPdf()).isEqualTo(UPDATED_ANEXO_PDF);
        assertThat(testProcesso.getAnexoPdfContentType()).isEqualTo(UPDATED_ANEXO_PDF_CONTENT_TYPE);
        assertThat(testProcesso.getUpload()).isEqualTo(UPDATED_UPLOAD);
    }

    @Test
    @Transactional
    void fullUpdateProcessoWithPatch() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo using partial update
        Processo partialUpdatedProcesso = new Processo();
        partialUpdatedProcesso.setId(processo.getId());

        partialUpdatedProcesso
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .anexoPdf(UPDATED_ANEXO_PDF)
            .anexoPdfContentType(UPDATED_ANEXO_PDF_CONTENT_TYPE)
            .upload(UPDATED_UPLOAD);

        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getNpu()).isEqualTo(UPDATED_NPU);
        assertThat(testProcesso.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testProcesso.getMunicipio()).isEqualTo(UPDATED_MUNICIPIO);
        assertThat(testProcesso.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testProcesso.getAnexoPdf()).isEqualTo(UPDATED_ANEXO_PDF);
        assertThat(testProcesso.getAnexoPdfContentType()).isEqualTo(UPDATED_ANEXO_PDF_CONTENT_TYPE);
        assertThat(testProcesso.getUpload()).isEqualTo(UPDATED_UPLOAD);
    }

    @Test
    @Transactional
    void patchNonExistingProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();
        processo.setId(count.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        int databaseSizeBeforeDelete = processoRepository.findAll().size();

        // Delete the processo
        restProcessoMockMvc
            .perform(delete(ENTITY_API_URL_ID, processo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
