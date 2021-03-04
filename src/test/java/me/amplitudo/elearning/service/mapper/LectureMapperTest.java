package me.amplitudo.elearning.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LectureMapperTest {

    private LectureMapper lectureMapper;

    @BeforeEach
    public void setUp() {
        lectureMapper = new LectureMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(lectureMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(lectureMapper.fromId(null)).isNull();
    }
}
