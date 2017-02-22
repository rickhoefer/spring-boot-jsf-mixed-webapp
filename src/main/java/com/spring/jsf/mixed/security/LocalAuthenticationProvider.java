package com.spring.jsf.mixed.security;

import com.spring.jsf.mixed.model.User;
import com.spring.jsf.mixed.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LocalAuthenticationProvider implements AuthenticationProvider {
    final Logger logger = Logger.getLogger(LocalAuthenticationProvider.class);
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        return getAuthentication(username, password);
    }

    private Authentication getAuthentication(String username, String password) {
        User user = userService.findByUsername(username);

        if (user == null) {
            logger.error("authenticate() - unknown provider name " + username);
            throw new BadCredentialsException("invalid provider");
        } else {
            if (StringUtils.isEmpty(password)) {
                logger.error("authenticate() - no password provider for provider " + username);
                throw new InsufficientAuthenticationException("No password for user");
            } else {
                if (password.equals(user.getPassword())) {


                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user, password, user.getAuthorities());
                    return authenticationToken;
                } else {
                    logger.error("authenticate() - invalid password for provider " + username + " [" + password + "]");
                    throw new BadCredentialsException("invalid credentials");
                }
            }
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

}