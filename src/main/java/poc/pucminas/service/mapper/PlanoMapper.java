package poc.pucminas.service.mapper;


import poc.pucminas.domain.*;
import poc.pucminas.service.dto.PlanoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plano} and its DTO {@link PlanoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlanoMapper extends EntityMapper<PlanoDTO, Plano> {



    default Plano fromId(Long id) {
        if (id == null) {
            return null;
        }
        Plano plano = new Plano();
        plano.setId(id);
        return plano;
    }
}
