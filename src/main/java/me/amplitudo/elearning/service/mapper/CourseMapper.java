package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, YearMapper.class, OrientationMapper.class, LectureMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "professor.id", target = "professorId")
    @Mapping(source = "assistant.id", target = "assistantId")
    @Mapping(source = "year.id", target = "yearId")
    @Mapping(source = "lectures.id", target = "lecturesId")
    CourseDTO toDto(Course course);

    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "removeNotifications", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "removeAssignments", ignore = true)
    @Mapping(source = "professorId", target = "professor")
    @Mapping(source = "assistantId", target = "assistant")
    @Mapping(source = "yearId", target = "year")
    @Mapping(target = "removeOrientations", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    @Mapping(source = "lecturesId", target = "lectures")
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
