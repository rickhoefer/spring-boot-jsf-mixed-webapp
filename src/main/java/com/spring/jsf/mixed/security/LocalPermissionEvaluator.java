package com.spring.jsf.mixed.security;

import org.apache.log4j.Logger;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class LocalPermissionEvaluator implements PermissionEvaluator {
    private Logger logger = Logger.getLogger(LocalPermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication arg0, Object arg1, Object arg2) {
        logger.info("arg1 " + arg1);
        logger.info("arg2 " + arg2);
        return true;
    }

    @Override
    public boolean hasPermission(Authentication arg0, Serializable arg1,
                                 String arg2, Object arg3) {
        logger.info("arg1 " + arg1);
        logger.info("arg2 " + arg2);
        logger.info("arg3 " + arg3);
        return true;
    }

}
