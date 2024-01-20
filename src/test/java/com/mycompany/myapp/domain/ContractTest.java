package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContractTestSamples.*;
import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void manageremployeeTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        contract.setManageremployee(employeeBack);
        assertThat(contract.getManageremployee()).isEqualTo(employeeBack);

        contract.manageremployee(null);
        assertThat(contract.getManageremployee()).isNull();
    }
}
