package me.amplitudo.elearning.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentMapperTest {

    private AssignmentMapper assignmentMapper;

    @BeforeEach
    public void setUp() {
        assignmentMapper = new AssignmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(assignmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(assignmentMapper.fromId(null)).isNull();
    }
}
