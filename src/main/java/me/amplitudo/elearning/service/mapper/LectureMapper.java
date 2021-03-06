package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.LectureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lecture} and its DTO {@link LectureDTO}.
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class, ProfileMapper.class})
public interface LectureMapper extends EntityMapper<LectureDTO, Lecture> {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "user.id", target = "userId")
    LectureDTO toDto(Lecture lecture);

    @Mapping(source = "courseId", target = "course")
    @Mapping(source = "userId", target = "user")
    Lecture toEntity(LectureDTO lectureDTO);

    default Lecture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lecture lecture = new Lecture();
        lecture.setId(id);
        return lecture;
    }
}
