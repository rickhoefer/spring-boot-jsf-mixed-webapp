package com.spring.jsf.mixed.service;

import com.spring.jsf.mixed.model.Role;
import com.spring.jsf.mixed.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @PersistenceContext
    private EntityManager em;

    // @Cacheable(value="roleList")
    // @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role save(Role role) {

        roleRepository.save(role);
        return role;
    }

    public Role findById(int Id) {
        return roleRepository.findById(Id);
    }
}
