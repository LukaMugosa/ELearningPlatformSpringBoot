package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.AssignmentProfile;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AssignmentProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentProfileRepository extends JpaRepository<AssignmentProfile, Long>, JpaSpecificationExecutor<AssignmentProfile> {
}
