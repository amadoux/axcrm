package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SocialCharges;
import com.mycompany.myapp.service.dto.SocialChargesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialCharges} and its DTO {@link SocialChargesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialChargesMapper extends EntityMapper<SocialChargesDTO, SocialCharges> {}
