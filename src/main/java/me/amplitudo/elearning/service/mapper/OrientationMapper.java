package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.OrientationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orientation} and its DTO {@link OrientationDTO}.
 */
@Mapper(componentModel = "spring", uses = {FacultyMapper.class})
public interface OrientationMapper extends EntityMapper<OrientationDTO, Orientation> {

    @Mapping(source = "faculty.id", target = "facultyId")
    OrientationDTO toDto(Orientation orientation);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    @Mapping(source = "facultyId", target = "faculty")
    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "removeFaculties", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "removeCourses", ignore = true)
    Orientation toEntity(OrientationDTO orientationDTO);

    default Orientation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Orientation orientation = new Orientation();
        orientation.setId(id);
        return orientation;
    }
}
