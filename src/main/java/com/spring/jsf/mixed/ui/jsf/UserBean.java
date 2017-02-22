package com.spring.jsf.mixed.ui.jsf;

import com.spring.jsf.mixed.model.Role;
import com.spring.jsf.mixed.model.User;
import com.spring.jsf.mixed.repository.RoleRepository;
import com.spring.jsf.mixed.repository.UserRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "user")
@SessionScoped
public class UserBean {

    @NotNull
    @NotEmpty
    private String fname;
    @NotNull
    @NotEmpty
    private String lname;
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @Size(min = 1)
    private String[] roles;

    @Autowired
    private UserRepository userModelRepository;

    @Autowired
    private RoleRepository roleModelRepository;

    @PostConstruct
    private void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);


    }

    public void preRenderView() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            // user = new UserModel();
        }
    }

    public List<String> getRolesList() {
        return roleModelRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
    }

    public void viewUser() {

        if (getUsername() != null && !getUsername().isEmpty()) {
            User user = userModelRepository.findByUsername(getUsername());
            setFname(user.getFname());
            setLname(user.getLname());
            setEmail(user.getEmail());
            setUsername(user.getUsername());

            List<String> rolesList = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            int size = rolesList.size();

            setRoles(rolesList.toArray(new String[size]));

        }

    }

    public void saveUser() {
        List<Role> roleModels = new ArrayList<>();
        for (String role : roles) {
            Role roleModel = roleModelRepository.findByName(role);
            if (roleModel != null) {
                roleModels.add(roleModel);
            }
        }

        User user = new User();
        user.setFname(getFname());
        user.setLname(getLname());
        user.setEmail(getEmail());
        user.setUsername(getUsername());
        user.setRoles(roleModels);

        userModelRepository.save(user);

        reset();

    }

    public void updateUser() {
        List<Role> roleModels = new ArrayList<>();
        for (String role : roles) {
            Role roleModel = roleModelRepository.findByName(role);
            if (roleModel != null) {
                roleModels.add(roleModel);
            }
        }

        User user = userModelRepository.findByUsername(getUsername());
        user.setFname(getFname());
        user.setLname(getLname());
        user.setEmail(getEmail());
        user.getRoles().clear();
        user.setRoles(roleModels);
        userModelRepository.save(user);
        reset();

    }


    public void reset() {
        setFname(null);
        setLname(null);
        setEmail(null);
        setUsername(null);
        setRoles(null);
    }


    /*getter and Setters*/

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
