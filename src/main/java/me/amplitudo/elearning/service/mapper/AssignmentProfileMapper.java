package me.amplitudo.elearning.service.mapper;


import me.amplitudo.elearning.domain.*;
import me.amplitudo.elearning.service.dto.AssignmentProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssignmentProfile} and its DTO {@link AssignmentProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {AssignmentMapper.class, ProfileMapper.class})
public interface AssignmentProfileMapper extends EntityMapper<AssignmentProfileDTO, AssignmentProfile> {

    @Mapping(source = "assignment.id", target = "assignmentId")
    @Mapping(source = "user.id", target = "userId")
    AssignmentProfileDTO toDto(AssignmentProfile assignmentProfile);

    @Mapping(source = "assignmentId", target = "assignment")
    @Mapping(source = "userId", target = "user")
    AssignmentProfile toEntity(AssignmentProfileDTO assignmentProfileDTO);

    default AssignmentProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        AssignmentProfile assignmentProfile = new AssignmentProfile();
        assignmentProfile.setId(id);
        return assignmentProfile;
    }
}
