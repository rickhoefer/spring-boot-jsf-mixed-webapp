package com.spring.jsf.mixed.repository;

import com.spring.jsf.mixed.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    List<Role> findAll();

    Role findById(int Id);

    Role findByName(String role);
}
