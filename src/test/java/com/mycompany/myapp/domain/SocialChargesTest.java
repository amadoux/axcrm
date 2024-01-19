package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EnterpriseTestSamples.*;
import static com.mycompany.myapp.domain.SocialChargesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialChargesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialCharges.class);
        SocialCharges socialCharges1 = getSocialChargesSample1();
        SocialCharges socialCharges2 = new SocialCharges();
        assertThat(socialCharges1).isNotEqualTo(socialCharges2);

        socialCharges2.setId(socialCharges1.getId());
        assertThat(socialCharges1).isEqualTo(socialCharges2);

        socialCharges2 = getSocialChargesSample2();
        assertThat(socialCharges1).isNotEqualTo(socialCharges2);
    }

    @Test
    void enterpriseTest() throws Exception {
        SocialCharges socialCharges = getSocialChargesRandomSampleGenerator();
        Enterprise enterpriseBack = getEnterpriseRandomSampleGenerator();

        socialCharges.setEnterprise(enterpriseBack);
        assertThat(socialCharges.getEnterprise()).isEqualTo(enterpriseBack);

        socialCharges.enterprise(null);
        assertThat(socialCharges.getEnterprise()).isNull();
    }
}
