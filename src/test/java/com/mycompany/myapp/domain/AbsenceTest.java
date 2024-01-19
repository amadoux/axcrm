package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AbsenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbsenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Absence.class);
        Absence absence1 = getAbsenceSample1();
        Absence absence2 = new Absence();
        assertThat(absence1).isNotEqualTo(absence2);

        absence2.setId(absence1.getId());
        assertThat(absence1).isEqualTo(absence2);

        absence2 = getAbsenceSample2();
        assertThat(absence1).isNotEqualTo(absence2);
    }
}
