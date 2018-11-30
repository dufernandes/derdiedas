package com.derdiedas.repository;

import com.derdiedas.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findAll(Pageable page);
}
