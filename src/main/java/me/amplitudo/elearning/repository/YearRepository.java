package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.Year;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Year entity.
 */
@SuppressWarnings("unused")
@Repository
public interface YearRepository extends JpaRepository<Year, Long>, JpaSpecificationExecutor<Year> {
}
