package com.spring.jsf.mixed.service;

import com.spring.jsf.mixed.model.Role;
import com.spring.jsf.mixed.model.User;
import com.spring.jsf.mixed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;

    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //@Transactional(readOnly = true)
    // @Cacheable(value="userList")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // @Secured("ADMIN")
    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User update(User user, Set<Role> roles) {
        for (Role r : roles) {
            Role role = em.merge(r);
            user.getRoles().add(role);
            user = em.merge(user);
        }
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("[Login] User : " + username + " not found!");
        }
        return user;
    }

}
