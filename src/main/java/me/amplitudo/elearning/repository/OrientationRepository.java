package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.Orientation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Orientation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrientationRepository extends JpaRepository<Orientation, Long>, JpaSpecificationExecutor<Orientation> {

//    @Query(value =
//        "select * from orientation o " +
//        "left join course_orientations co " +
//        "on o.id = co.orientations_id " +
//        "where co.course_id = :courseId"
//        , nativeQuery = true)
    Page<Orientation> findAllByCoursesId(Pageable pageable, @Param("courseId") Long courseId);

    boolean existsById(Long id);
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
