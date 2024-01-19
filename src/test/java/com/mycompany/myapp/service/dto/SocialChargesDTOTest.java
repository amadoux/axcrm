package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialChargesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialChargesDTO.class);
        SocialChargesDTO socialChargesDTO1 = new SocialChargesDTO();
        socialChargesDTO1.setId(1L);
        SocialChargesDTO socialChargesDTO2 = new SocialChargesDTO();
        assertThat(socialChargesDTO1).isNotEqualTo(socialChargesDTO2);
        socialChargesDTO2.setId(socialChargesDTO1.getId());
        assertThat(socialChargesDTO1).isEqualTo(socialChargesDTO2);
        socialChargesDTO2.setId(2L);
        assertThat(socialChargesDTO1).isNotEqualTo(socialChargesDTO2);
        socialChargesDTO1.setId(null);
        assertThat(socialChargesDTO1).isNotEqualTo(socialChargesDTO2);
    }
}
