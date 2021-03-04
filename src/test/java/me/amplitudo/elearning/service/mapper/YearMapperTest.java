package me.amplitudo.elearning.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class YearMapperTest {

    private YearMapper yearMapper;

    @BeforeEach
    public void setUp() {
        yearMapper = new YearMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(yearMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(yearMapper.fromId(null)).isNull();
    }
}
