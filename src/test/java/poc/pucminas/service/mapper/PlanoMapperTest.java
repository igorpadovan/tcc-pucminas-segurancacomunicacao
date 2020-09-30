package poc.pucminas.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PlanoMapperTest {

    private PlanoMapper planoMapper;

    @BeforeEach
    public void setUp() {
        planoMapper = new PlanoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(planoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(planoMapper.fromId(null)).isNull();
    }
}
