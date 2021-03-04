package me.amplitudo.elearning.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OrientationMapperTest {

    private OrientationMapper orientationMapper;

    @BeforeEach
    public void setUp() {
        orientationMapper = new OrientationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(orientationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orientationMapper.fromId(null)).isNull();
    }
}
