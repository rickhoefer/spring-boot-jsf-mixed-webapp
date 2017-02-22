package com.spring.jsf.mixed.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger logger = Logger.getLogger(AuthSuccessHandler.class);
    private String targetUrl = "/index.xhtml";
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        return targetUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        logger.info("CRUENT USER : [" + auth.getName() + "] ROLE : " + role);
        super.setAlwaysUseDefaultTargetUrl(true);
        //super.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);

/*
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        String targetUrlParam = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParam != null && StringUtils.hasText(request.getParameter(targetUrlParam)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return;
        }
        clearAuthenticationAttributes(request);*/
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;


    }


}