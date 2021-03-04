package me.amplitudo.elearning.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class OrientationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrientationDTO.class);
        OrientationDTO orientationDTO1 = new OrientationDTO();
        orientationDTO1.setId(1L);
        OrientationDTO orientationDTO2 = new OrientationDTO();
        assertThat(orientationDTO1).isNotEqualTo(orientationDTO2);
        orientationDTO2.setId(orientationDTO1.getId());
        assertThat(orientationDTO1).isEqualTo(orientationDTO2);
        orientationDTO2.setId(2L);
        assertThat(orientationDTO1).isNotEqualTo(orientationDTO2);
        orientationDTO1.setId(null);
        assertThat(orientationDTO1).isNotEqualTo(orientationDTO2);
    }
}
