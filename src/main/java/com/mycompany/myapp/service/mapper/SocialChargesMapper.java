package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Enterprise;
import com.mycompany.myapp.domain.SocialCharges;
import com.mycompany.myapp.service.dto.EnterpriseDTO;
import com.mycompany.myapp.service.dto.SocialChargesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialCharges} and its DTO {@link SocialChargesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialChargesMapper extends EntityMapper<SocialChargesDTO, SocialCharges> {
    @Mapping(target = "enterprise", source = "enterprise", qualifiedByName = "enterpriseCompanyName")
    SocialChargesDTO toDto(SocialCharges s);

    @Named("enterpriseCompanyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    EnterpriseDTO toDtoEnterpriseCompanyName(Enterprise enterprise);
}
