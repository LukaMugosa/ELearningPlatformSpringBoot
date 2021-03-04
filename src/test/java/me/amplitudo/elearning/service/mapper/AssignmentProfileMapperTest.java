package me.amplitudo.elearning.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentProfileMapperTest {

    private AssignmentProfileMapper assignmentProfileMapper;

    @BeforeEach
    public void setUp() {
        assignmentProfileMapper = new AssignmentProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(assignmentProfileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(assignmentProfileMapper.fromId(null)).isNull();
    }
}
