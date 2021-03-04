package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.YearDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Year} and its DTO {@link YearDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface YearMapper extends EntityMapper<YearDTO, Year> {


    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    Year toEntity(YearDTO yearDTO);

    default Year fromId(Long id) {
        if (id == null) {
            return null;
        }
        Year year = new Year();
        year.setId(id);
        return year;
    }
}
