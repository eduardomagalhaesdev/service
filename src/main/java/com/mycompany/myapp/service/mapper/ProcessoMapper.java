package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Processo;
import com.mycompany.myapp.service.dto.ProcessoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Processo} and its DTO {@link ProcessoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessoMapper extends EntityMapper<ProcessoDTO, Processo> {}
