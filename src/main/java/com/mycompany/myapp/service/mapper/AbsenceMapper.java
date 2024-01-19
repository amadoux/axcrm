package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Absence;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.service.dto.AbsenceDTO;
import com.mycompany.myapp.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeEmail")
    AbsenceDTO toDto(Absence s);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);
}
