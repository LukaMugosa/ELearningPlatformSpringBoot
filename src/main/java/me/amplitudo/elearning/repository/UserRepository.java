package me.amplitudo.elearning.repository;

import me.amplitudo.elearning.domain.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    Page<User> findAllByAuthoritiesNameOrAuthoritiesName(Pageable pageable, String name1, String name2);

    @Query(value =
       "select * from jhi_user u where u.id in (\n" +
       "\tselect p.user_id from profile p where p.faculty_id in (\n" +
       "\t\tselect fu.faculty_id from faculty_users fu where fu.users_id = :userId\n" +
       "    )\n" +
       ")\n" +
       "and u.id in \n" +
       "(\n" +
       "\tselect ua.user_id from jhi_user_authority ua where authority_name = 'ROLE_STUDENT'\n" +
       ")"
        ,nativeQuery = true
    )
    Page<User> findMyStudents(Pageable pageable, @Param("userId") Long userId);

}
