package me.amplitudo.elearning.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class FacultyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Faculty.class);
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        Faculty faculty2 = new Faculty();
        faculty2.setId(faculty1.getId());
        assertThat(faculty1).isEqualTo(faculty2);
        faculty2.setId(2L);
        assertThat(faculty1).isNotEqualTo(faculty2);
        faculty1.setId(null);
        assertThat(faculty1).isNotEqualTo(faculty2);
    }
}
