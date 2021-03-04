package me.amplitudo.elearning.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FacultyMapperTest {

    private FacultyMapper facultyMapper;

    @BeforeEach
    public void setUp() {
        facultyMapper = new FacultyMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(facultyMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(facultyMapper.fromId(null)).isNull();
    }
}
