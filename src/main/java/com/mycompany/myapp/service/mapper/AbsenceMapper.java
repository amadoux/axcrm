package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Absence;
import com.mycompany.myapp.service.dto.AbsenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {}
