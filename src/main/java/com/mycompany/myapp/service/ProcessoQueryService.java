package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Processo;
import com.mycompany.myapp.repository.ProcessoRepository;
import com.mycompany.myapp.service.criteria.ProcessoCriteria;
import com.mycompany.myapp.service.dto.ProcessoDTO;
import com.mycompany.myapp.service.mapper.ProcessoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Processo} entities in the database.
 * The main input is a {@link ProcessoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProcessoDTO} or a {@link Page} of {@link ProcessoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProcessoQueryService extends QueryService<Processo> {

    private final Logger log = LoggerFactory.getLogger(ProcessoQueryService.class);

    private final ProcessoRepository processoRepository;

    private final ProcessoMapper processoMapper;

    public ProcessoQueryService(ProcessoRepository processoRepository, ProcessoMapper processoMapper) {
        this.processoRepository = processoRepository;
        this.processoMapper = processoMapper;
    }

    /**
     * Return a {@link List} of {@link ProcessoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessoDTO> findByCriteria(ProcessoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Processo> specification = createSpecification(criteria);
        return processoMapper.toDto(processoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProcessoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcessoDTO> findByCriteria(ProcessoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Processo> specification = createSpecification(criteria);
        return processoRepository.findAll(specification, page).map(processoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProcessoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Processo> specification = createSpecification(criteria);
        return processoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProcessoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Processo> createSpecification(ProcessoCriteria criteria) {
        Specification<Processo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Processo_.id));
            }
            if (criteria.getNpu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNpu(), Processo_.npu));
            }
            if (criteria.getDataCadastro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCadastro(), Processo_.dataCadastro));
            }
            if (criteria.getMunicipio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMunicipio(), Processo_.municipio));
            }
            if (criteria.getUf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUf(), Processo_.uf));
            }
            if (criteria.getUpload() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpload(), Processo_.upload));
            }
        }
        return specification;
    }
}
