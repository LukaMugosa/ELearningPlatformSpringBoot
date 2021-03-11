package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query("select course from Course course where course.professor.login = ?#{principal.username}")
    List<Course> findByProfessorIsCurrentUser();

    @Query("select course from Course course where course.assistant.login = ?#{principal.username}")
    List<Course> findByAssistantIsCurrentUser();

    @Query(value = "select distinct course from Course course left join fetch course.orientations left join fetch course.users",
        countQuery = "select count(distinct course) from Course course")
    Page<Course> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct course from Course course left join fetch course.orientations left join fetch course.users")
    List<Course> findAllWithEagerRelationships();

    @Query("select course from Course course left join fetch course.orientations left join fetch course.users where course.id =:id")
    Optional<Course> findOneWithEagerRelationships(@Param("id") Long id);

    Integer countAllByName(String name);

    Integer countAllByNameAndIdNot(String name, Long id);

    boolean existsById(Long id);

    @Query(value = "select count(*) from course_orientations co where co.orientations_id = ?1 and co.course_id = ?2",
        nativeQuery = true)
    Integer countAllByOrientationsIdAndCourseId(Long orientationId, Long courseId);

    @Query(
        value = "select c.* from course c\n" +
        "where c.id not in\n" +
        "(\n" +
        "    select cu.course_id from course_users cu\n" +
        "    where cu.users_id = :userId\n" +
        ")\n" +
        "and ( c.professor_id = :professorId or c.assistant_id = :professorId )"
        ,nativeQuery = true
    )
    Page<Course> findAllCoursesStudentDoesNotHave(Pageable pageable, @Param("professorId") Long professorId, @Param("userId") Long userId);

}
