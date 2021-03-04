package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.Faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Faculty entity.
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long>, JpaSpecificationExecutor<Faculty> {

    @Query(value = "select distinct faculty from Faculty faculty left join fetch faculty.orientationFaculties left join fetch faculty.users",
        countQuery = "select count(distinct faculty) from Faculty faculty")
    Page<Faculty> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct faculty from Faculty faculty left join fetch faculty.orientationFaculties left join fetch faculty.users")
    List<Faculty> findAllWithEagerRelationships();

    @Query("select faculty from Faculty faculty left join fetch faculty.orientationFaculties left join fetch faculty.users where faculty.id =:id")
    Optional<Faculty> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Faculty> findOneByName(String name);

    Integer countAllByName(String name);

    Integer countAllByNameAndIdNot(String name, Long id);

}
