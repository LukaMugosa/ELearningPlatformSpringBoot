package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.Orientation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Orientation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrientationRepository extends JpaRepository<Orientation, Long>, JpaSpecificationExecutor<Orientation> {
}
