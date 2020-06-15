package com.springboot.practice.repository;

import com.springboot.practice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findOneById(Integer userId);

    Optional<User> findOneByUsername(String username);

    List<User> findAll();
}
