package poc.pucminas.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import poc.pucminas.web.rest.TestUtil;

public class PlanoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoDTO.class);
        PlanoDTO planoDTO1 = new PlanoDTO();
        planoDTO1.setId(1L);
        PlanoDTO planoDTO2 = new PlanoDTO();
        assertThat(planoDTO1).isNotEqualTo(planoDTO2);
        planoDTO2.setId(planoDTO1.getId());
        assertThat(planoDTO1).isEqualTo(planoDTO2);
        planoDTO2.setId(2L);
        assertThat(planoDTO1).isNotEqualTo(planoDTO2);
        planoDTO1.setId(null);
        assertThat(planoDTO1).isNotEqualTo(planoDTO2);
    }
}
