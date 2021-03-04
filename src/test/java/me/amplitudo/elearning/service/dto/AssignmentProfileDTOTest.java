package me.amplitudo.elearning.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class AssignmentProfileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentProfileDTO.class);
        AssignmentProfileDTO assignmentProfileDTO1 = new AssignmentProfileDTO();
        assignmentProfileDTO1.setId(1L);
        AssignmentProfileDTO assignmentProfileDTO2 = new AssignmentProfileDTO();
        assertThat(assignmentProfileDTO1).isNotEqualTo(assignmentProfileDTO2);
        assignmentProfileDTO2.setId(assignmentProfileDTO1.getId());
        assertThat(assignmentProfileDTO1).isEqualTo(assignmentProfileDTO2);
        assignmentProfileDTO2.setId(2L);
        assertThat(assignmentProfileDTO1).isNotEqualTo(assignmentProfileDTO2);
        assignmentProfileDTO1.setId(null);
        assertThat(assignmentProfileDTO1).isNotEqualTo(assignmentProfileDTO2);
    }
}
