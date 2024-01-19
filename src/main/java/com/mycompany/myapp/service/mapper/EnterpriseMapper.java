package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Enterprise;
import com.mycompany.myapp.service.dto.EnterpriseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enterprise} and its DTO {@link EnterpriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnterpriseMapper extends EntityMapper<EnterpriseDTO, Enterprise> {}
