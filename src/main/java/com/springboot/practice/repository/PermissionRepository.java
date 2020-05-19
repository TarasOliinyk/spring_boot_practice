package com.springboot.practice.repository;

import com.springboot.practice.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {

    Optional<Permission> findOneById(Integer id);
}
