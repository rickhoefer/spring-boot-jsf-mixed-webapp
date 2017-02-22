package com.spring.jsf.mixed.ui.jsf;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "login")
@SessionScoped

public class LoginBean {

    public void preRenderView() {

        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
