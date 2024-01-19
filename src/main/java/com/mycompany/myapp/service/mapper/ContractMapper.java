package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Contract;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.service.dto.ContractDTO;
import com.mycompany.myapp.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeEmail")
    ContractDTO toDto(Contract s);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);
}
