package me.amplitudo.elearning.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class AssignmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentDTO.class);
        AssignmentDTO assignmentDTO1 = new AssignmentDTO();
        assignmentDTO1.setId(1L);
        AssignmentDTO assignmentDTO2 = new AssignmentDTO();
        assertThat(assignmentDTO1).isNotEqualTo(assignmentDTO2);
        assignmentDTO2.setId(assignmentDTO1.getId());
        assertThat(assignmentDTO1).isEqualTo(assignmentDTO2);
        assignmentDTO2.setId(2L);
        assertThat(assignmentDTO1).isNotEqualTo(assignmentDTO2);
        assignmentDTO1.setId(null);
        assertThat(assignmentDTO1).isNotEqualTo(assignmentDTO2);
    }
}
