package com.springboot.practice.repository;

import com.springboot.practice.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findOneById(Integer id);

    List<Role> findAll();

    List<Role> findAllByIdIn(List<Integer> ids);
}
