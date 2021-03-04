package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.FacultyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Faculty} and its DTO {@link FacultyDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrientationMapper.class, ProfileMapper.class, BuildingMapper.class})
public interface FacultyMapper extends EntityMapper<FacultyDTO, Faculty> {

    @Mapping(source = "building.id", target = "buildingId")
    @Mapping(source = "building.name", target = "buildingName")
    FacultyDTO toDto(Faculty faculty);

    @Mapping(source = "buildingId", target = "building.id")
    @Mapping(source = "buildingName", target = "building.name")
    Faculty toEntity(FacultyDTO facultyDTO);

    default Faculty fromId(Long id) {
        if (id == null) {
            return null;
        }
        Faculty faculty = new Faculty();
        faculty.setId(id);
        return faculty;
    }
}
