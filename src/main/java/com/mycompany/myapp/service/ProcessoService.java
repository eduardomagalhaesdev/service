package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProcessoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Processo}.
 */
public interface ProcessoService {
    /**
     * Save a processo.
     *
     * @param processoDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessoDTO save(ProcessoDTO processoDTO);

    /**
     * Updates a processo.
     *
     * @param processoDTO the entity to update.
     * @return the persisted entity.
     */
    ProcessoDTO update(ProcessoDTO processoDTO);

    /**
     * Partially updates a processo.
     *
     * @param processoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProcessoDTO> partialUpdate(ProcessoDTO processoDTO);

    /**
     * Get all the processos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" processo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessoDTO> findOne(Long id);

    /**
     * Delete the "id" processo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
