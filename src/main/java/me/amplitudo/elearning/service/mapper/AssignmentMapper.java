package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.AssignmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assignment} and its DTO {@link AssignmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CourseMapper.class})
public interface AssignmentMapper extends EntityMapper<AssignmentDTO, Assignment> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "course.id", target = "courseId")
    AssignmentDTO toDto(Assignment assignment);

    @Mapping(target = "assignmentProfiles", ignore = true)
    @Mapping(target = "removeAssignmentProfiles", ignore = true)
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "courseId", target = "course")
    Assignment toEntity(AssignmentDTO assignmentDTO);

    default Assignment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Assignment assignment = new Assignment();
        assignment.setId(id);
        return assignment;
    }
}
