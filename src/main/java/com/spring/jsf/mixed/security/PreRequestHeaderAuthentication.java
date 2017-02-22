package com.spring.jsf.mixed.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PreRequestHeaderAuthentication extends RequestHeaderAuthenticationFilter {


    private LocalPreAuthenticationProvider localPreAuthenticationProvider;

    @Autowired
    public PreRequestHeaderAuthentication(AuthenticationManager authenticationManager, LocalPreAuthenticationProvider localPreAuthenticationProvider) {
        setAuthenticationManager(authenticationManager);
        this.localPreAuthenticationProvider = localPreAuthenticationProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {


        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            PreAuthenticatedAuthenticationToken authRequest = new PreAuthenticatedAuthenticationToken(
                    "admin", "admin");
            authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));


            Authentication authResult = localPreAuthenticationProvider.authenticate(authRequest);

            successfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response, authResult);

        }
        chain.doFilter(request, response);
    }


}