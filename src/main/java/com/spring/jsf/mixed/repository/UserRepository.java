package com.spring.jsf.mixed.repository;


import com.spring.jsf.mixed.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    List<User> findAll();

    @RestResource(path = "by-username")
    User findByUsername(String username);

    User findById(Integer id);

}
