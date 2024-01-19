package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.EnterpriseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnterpriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enterprise.class);
        Enterprise enterprise1 = getEnterpriseSample1();
        Enterprise enterprise2 = new Enterprise();
        assertThat(enterprise1).isNotEqualTo(enterprise2);

        enterprise2.setId(enterprise1.getId());
        assertThat(enterprise1).isEqualTo(enterprise2);

        enterprise2 = getEnterpriseSample2();
        assertThat(enterprise1).isNotEqualTo(enterprise2);
    }

    @Test
    void employeeTest() throws Exception {
        Enterprise enterprise = getEnterpriseRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        enterprise.setEmployee(employeeBack);
        assertThat(enterprise.getEmployee()).isEqualTo(employeeBack);

        enterprise.employee(null);
        assertThat(enterprise.getEmployee()).isNull();
    }
}
