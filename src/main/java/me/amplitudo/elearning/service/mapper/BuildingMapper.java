package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.BuildingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Building} and its DTO {@link BuildingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BuildingMapper extends EntityMapper<BuildingDTO, Building> {


    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "removeFaculties", ignore = true)
    Building toEntity(BuildingDTO buildingDTO);

    default Building fromId(Long id) {
        if (id == null) {
            return null;
        }
        Building building = new Building();
        building.setId(id);
        return building;
    }
}
