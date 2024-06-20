package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Processo;
import com.mycompany.myapp.repository.ProcessoRepository;
import com.mycompany.myapp.service.ProcessoService;
import com.mycompany.myapp.service.dto.ProcessoDTO;
import com.mycompany.myapp.service.mapper.ProcessoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Processo}.
 */
@Service
@Transactional
public class ProcessoServiceImpl implements ProcessoService {

    private final Logger log = LoggerFactory.getLogger(ProcessoServiceImpl.class);

    private final ProcessoRepository processoRepository;

    private final ProcessoMapper processoMapper;

    public ProcessoServiceImpl(ProcessoRepository processoRepository, ProcessoMapper processoMapper) {
        this.processoRepository = processoRepository;
        this.processoMapper = processoMapper;
    }

    @Override
    public ProcessoDTO save(ProcessoDTO processoDTO) {
        log.debug("Request to save Processo : {}", processoDTO);
        Processo processo = processoMapper.toEntity(processoDTO);
        processo = processoRepository.save(processo);
        return processoMapper.toDto(processo);
    }

    @Override
    public ProcessoDTO update(ProcessoDTO processoDTO) {
        log.debug("Request to update Processo : {}", processoDTO);
        Processo processo = processoMapper.toEntity(processoDTO);
        processo = processoRepository.save(processo);
        return processoMapper.toDto(processo);
    }

    @Override
    public Optional<ProcessoDTO> partialUpdate(ProcessoDTO processoDTO) {
        log.debug("Request to partially update Processo : {}", processoDTO);

        return processoRepository
            .findById(processoDTO.getId())
            .map(existingProcesso -> {
                processoMapper.partialUpdate(existingProcesso, processoDTO);

                return existingProcesso;
            })
            .map(processoRepository::save)
            .map(processoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Processos");
        return processoRepository.findAll(pageable).map(processoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessoDTO> findOne(Long id) {
        log.debug("Request to get Processo : {}", id);
        return processoRepository.findById(id).map(processoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Processo : {}", id);
        processoRepository.deleteById(id);
    }
}
