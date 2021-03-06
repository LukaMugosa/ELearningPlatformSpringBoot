package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.ProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, YearMapper.class, OrientationMapper.class})
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "year.id", target = "yearId")
    @Mapping(source = "faculty.id", target = "facultyId")
    ProfileDTO toDto(Profile profile);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "assignmentProfiles", ignore = true)
    @Mapping(target = "removeAssignmentProfiles", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "removeLectures", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "removeNotifications", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "removeAssignments", ignore = true)
    @Mapping(source = "yearId", target = "year")
    @Mapping(source = "facultyId", target = "faculty")
    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "removeFaculties", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "removeCourses", ignore = true)
    Profile toEntity(ProfileDTO profileDTO);

    default Profile fromId(Long id) {
        if (id == null) {
            return null;
        }
        Profile profile = new Profile();
        profile.setId(id);
        return profile;
    }
}
