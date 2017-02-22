package com.spring.jsf.mixed.security;

import com.spring.jsf.mixed.model.User;
import com.spring.jsf.mixed.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class LocalLdapAuthentication {
    final Logger logger = Logger.getLogger(LocalLdapAuthentication.class);


    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://ldap.forumsys.com:389/dc=example,dc=com"); //ldap://192.168.150.11/dc=krg,dc=net
        contextSource.setUserDn("cn=read-only-admin,dc=example,dc=com");
        contextSource.setPassword("password");
        contextSource.setBase("dc=example,dc=com");

        contextSource.afterPropertiesSet();
        return contextSource;
    }


    @Bean
    public BindAuthenticator bindAuthenticator(DefaultSpringSecurityContextSource contextSource) {
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        // //authenticator.setUserSearch(new FilterBasedLdapUserSearch("dc=krg,dc=net", "(uid={0})", contextSource)); //uid={0},dc=krg,dc=net
        authenticator.setUserSearch(new FilterBasedLdapUserSearch("ou=mathematicians", "(uid={0})", contextSource));
        //authenticator.setUserDnPatterns(new String[]{"uid={0},ou=mathematicians"});
        return authenticator;
    }

    @Bean
    public LdapAuthoritiesPopulator authoritiesPopulator() {
        LdapAuthoritiesPopulator authoritiesPopulator = new LocalLdapAuthoritiesPopulator();
        return authoritiesPopulator;
    }

    @Bean
    public LdapAuthenticationProvider localLdapAuthenticationProvider(BindAuthenticator bindAuthenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
        LdapAuthenticationProvider provider = new LdapAuthenticationProvider(bindAuthenticator, authoritiesPopulator);
        return provider;
    }


    public class LocalLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {
        @Autowired
        private UserService userService;

        @Override
        public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
            User user = userService.findByUsername(username);
            return user != null ? user.getAuthorities() : Collections.EMPTY_LIST;
        }
    }

}
