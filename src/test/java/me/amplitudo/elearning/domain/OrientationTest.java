package me.amplitudo.elearning.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class OrientationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orientation.class);
        Orientation orientation1 = new Orientation();
        orientation1.setId(1L);
        Orientation orientation2 = new Orientation();
        orientation2.setId(orientation1.getId());
        assertThat(orientation1).isEqualTo(orientation2);
        orientation2.setId(2L);
        assertThat(orientation1).isNotEqualTo(orientation2);
        orientation1.setId(null);
        assertThat(orientation1).isNotEqualTo(orientation2);
    }
}
