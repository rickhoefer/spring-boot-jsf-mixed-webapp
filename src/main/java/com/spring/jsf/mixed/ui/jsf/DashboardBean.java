package com.spring.jsf.mixed.ui.jsf;

import com.spring.jsf.mixed.model.User;
import com.spring.jsf.mixed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

@ManagedBean(name = "dashboard")
@SessionScoped
public class DashboardBean implements Serializable {

    private String environment;
    private int dataModel;
    private String locale;
    private String safeMode;

    private User user;

    @Autowired
    private UserService userService;

    @ManagedProperty("#{translation}")
    private ResourceBundle translation;

    @PostConstruct
    private void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        Locale applicationLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

        locale = applicationLocale.getDisplayLanguage();
        environmentSwitcher("Prod");
        safeModeSwitcher("on");

        userAth();

    }


    private void userAth() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        user = (User) auth.getPrincipal();
        /*if (user == null) {
            String username = "admin";
            String password = "admin";
            user = userService.findByUsername(username);
            if (password.equals(user.getPassword())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user, password, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }*/
    }


    public void environmentSwitcher(String env) {
        environment = env;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("environment", env);
    }

    public void test() {
        ResourceBundle securityBundle = ResourceBundle.getBundle("com.cr.util.LocalResourceBundle");
        String errorMessage = securityBundle.getString("messages.error");
    }


    public void languageSwitcher(String code) {
        Locale applicationLocale = null;
        if (code.equals("en")) {
            applicationLocale = Locale.ENGLISH;
        } else if (code.equals("nb")) {
            applicationLocale = new Locale("nb");

        }
        FacesContext.getCurrentInstance()
                .getViewRoot().setLocale(applicationLocale);

        locale = applicationLocale.getDisplayLanguage();

    }

    public void safeModeSwitcher(String safeMode) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (safeMode.equals("on")) {
            context.getExternalContext().getSessionMap().put("safeMode", true);
        } else if (safeMode.equals("off")) {
            context.getExternalContext().getSessionMap().put("safeMode", false);
        }
        this.safeMode = safeMode;
    }


    public ResourceBundle getTranslation() {
        return translation;
    }

    public void setTranslation(ResourceBundle translation) {
        this.translation = translation;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public int getDataModel() {
        return dataModel;
    }

    public void setDataModel(int dataModel) {
        this.dataModel = dataModel;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getSafeMode() {
        return safeMode;
    }

    public void setSafeMode(String safeMode) {
        this.safeMode = safeMode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
