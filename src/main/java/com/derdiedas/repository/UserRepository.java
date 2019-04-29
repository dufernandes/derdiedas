package com.derdiedas.repository;

import com.derdiedas.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository related to the {@link User} entity.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find a user based on her/his email. If no match
     * is found, null is returned.
     *
     * @param email email uniquely identifying the user
     * @return Optional holding entity {@link User}
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a list of {@link User} entities using
     * a paged search.
     *
     * @param page {@link Pageable} object for setting up the page query
     * @return Paged List of users.
     */
    Page<User> findAll(Pageable page);
}
