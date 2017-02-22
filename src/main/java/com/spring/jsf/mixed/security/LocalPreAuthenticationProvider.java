package com.spring.jsf.mixed.security;

import com.spring.jsf.mixed.model.Role;
import com.spring.jsf.mixed.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class LocalPreAuthenticationProvider extends PreAuthenticatedAuthenticationProvider {


    public LocalPreAuthenticationProvider() {
        UserDetailsByNameServiceWrapper userDetailsServiceWrapper = new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(customUserDetailsService());
        this.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper);
    }


    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    public class CustomUserDetailsService implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Role roleModel = new Role();
            roleModel.setName("ADMIM");

            User user = new User();
            user.getRoles().add(roleModel);
            user.setFname("Admin");
            user.setLname("User");
            user.setUsername("admin");
            user.setPassword("admin");
            user.setEmail("admin@admin.com");

            return user;
        }
    }
}
