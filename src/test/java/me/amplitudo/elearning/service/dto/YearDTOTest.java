package me.amplitudo.elearning.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class YearDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(YearDTO.class);
        YearDTO yearDTO1 = new YearDTO();
        yearDTO1.setId(1L);
        YearDTO yearDTO2 = new YearDTO();
        assertThat(yearDTO1).isNotEqualTo(yearDTO2);
        yearDTO2.setId(yearDTO1.getId());
        assertThat(yearDTO1).isEqualTo(yearDTO2);
        yearDTO2.setId(2L);
        assertThat(yearDTO1).isNotEqualTo(yearDTO2);
        yearDTO1.setId(null);
        assertThat(yearDTO1).isNotEqualTo(yearDTO2);
    }
}
