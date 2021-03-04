package me.amplitudo.elearning.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class AssignmentProfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentProfile.class);
        AssignmentProfile assignmentProfile1 = new AssignmentProfile();
        assignmentProfile1.setId(1L);
        AssignmentProfile assignmentProfile2 = new AssignmentProfile();
        assignmentProfile2.setId(assignmentProfile1.getId());
        assertThat(assignmentProfile1).isEqualTo(assignmentProfile2);
        assignmentProfile2.setId(2L);
        assertThat(assignmentProfile1).isNotEqualTo(assignmentProfile2);
        assignmentProfile1.setId(null);
        assertThat(assignmentProfile1).isNotEqualTo(assignmentProfile2);
    }
}
