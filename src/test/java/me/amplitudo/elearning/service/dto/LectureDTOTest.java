package me.amplitudo.elearning.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import me.amplitudo.elearning.web.rest.TestUtil;

public class LectureDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LectureDTO.class);
        LectureDTO lectureDTO1 = new LectureDTO();
        lectureDTO1.setId(1L);
        LectureDTO lectureDTO2 = new LectureDTO();
        assertThat(lectureDTO1).isNotEqualTo(lectureDTO2);
        lectureDTO2.setId(lectureDTO1.getId());
        assertThat(lectureDTO1).isEqualTo(lectureDTO2);
        lectureDTO2.setId(2L);
        assertThat(lectureDTO1).isNotEqualTo(lectureDTO2);
        lectureDTO1.setId(null);
        assertThat(lectureDTO1).isNotEqualTo(lectureDTO2);
    }
}
