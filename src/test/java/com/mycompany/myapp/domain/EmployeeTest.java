package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.EnterpriseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void managerTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employee.addManager(employeeBack);
        assertThat(employee.getManagers()).containsOnly(employeeBack);
        assertThat(employeeBack.getEmployee()).isEqualTo(employee);

        employee.removeManager(employeeBack);
        assertThat(employee.getManagers()).doesNotContain(employeeBack);
        assertThat(employeeBack.getEmployee()).isNull();

        employee.managers(new HashSet<>(Set.of(employeeBack)));
        assertThat(employee.getManagers()).containsOnly(employeeBack);
        assertThat(employeeBack.getEmployee()).isEqualTo(employee);

        employee.setManagers(new HashSet<>());
        assertThat(employee.getManagers()).doesNotContain(employeeBack);
        assertThat(employeeBack.getEmployee()).isNull();
    }

    @Test
    void enterpriseTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Enterprise enterpriseBack = getEnterpriseRandomSampleGenerator();

        employee.addEnterprise(enterpriseBack);
        assertThat(employee.getEnterprises()).containsOnly(enterpriseBack);
        assertThat(enterpriseBack.getEmployee()).isEqualTo(employee);

        employee.removeEnterprise(enterpriseBack);
        assertThat(employee.getEnterprises()).doesNotContain(enterpriseBack);
        assertThat(enterpriseBack.getEmployee()).isNull();

        employee.enterprises(new HashSet<>(Set.of(enterpriseBack)));
        assertThat(employee.getEnterprises()).containsOnly(enterpriseBack);
        assertThat(enterpriseBack.getEmployee()).isEqualTo(employee);

        employee.setEnterprises(new HashSet<>());
        assertThat(employee.getEnterprises()).doesNotContain(enterpriseBack);
        assertThat(enterpriseBack.getEmployee()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employee.setEmployee(employeeBack);
        assertThat(employee.getEmployee()).isEqualTo(employeeBack);

        employee.employee(null);
        assertThat(employee.getEmployee()).isNull();
    }
}
