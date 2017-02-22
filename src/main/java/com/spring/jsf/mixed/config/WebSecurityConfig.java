package com.spring.jsf.mixed.config;

import com.spring.jsf.mixed.security.AuthFailureHandler;
import com.spring.jsf.mixed.security.AuthSuccessHandler;
import com.spring.jsf.mixed.security.LocalAuthenticationProvider;
import com.spring.jsf.mixed.security.PreRequestHeaderAuthentication;
import com.spring.jsf.mixed.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@DependsOn(value = {"localAuthenticationProvider", "localLdapAuthenticationProvider"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    static final Logger logger = Logger.getLogger(WebSecurityConfig.class);


    @Autowired
    @Lazy
    @Qualifier("localLdapAuthenticationProvider")
    public LdapAuthenticationProvider ldapAuthenticationProvider;


    @Autowired
    @Lazy
    private LocalAuthenticationProvider localAuthenticationProvider;

    @Autowired
    @Lazy
    private DataSource dataSource;

    @Autowired
    @Lazy
    private UserService userService;


    @Autowired
    @Lazy
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    @Lazy
    private AuthFailureHandler authFailureHandler;

    @Autowired
    @Lazy
    private PreRequestHeaderAuthentication preRequestHeaderAuthentication;


    @Bean
    @Override
    @Order(1)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");

        auth.userDetailsService(userService);
        auth.authenticationProvider(localAuthenticationProvider);

      /*   auth.authenticationProvider(ldapAuthenticationProvider);*/

    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/javax.faces.resource/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*http.addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);
        .antMatchers("/login/impersonate*").hasRole("ADMIN")
        .antMatchers("/logout/impersonate*").authenticated()*/


/*        http.addFilterBefore(preRequestHeaderAuthentication,
                RequestHeaderAuthenticationFilter.class);*/

        http.headers()
                .addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(
                        new AntPathRequestMatcher("/javax.faces.resource/**"),
                        new HeaderWriter() {
                            @Override
                            public void writeHeaders(HttpServletRequest request,
                                                     HttpServletResponse response) {
                                response.addHeader("Cache-Control", "private, max-age=0");
                            }
                        }))
                .defaultsDisabled();

/*

        http.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "script-src 'self'"));
*/

        http.csrf().disable()
                .authorizeRequests()//
                .antMatchers("/javax.faces.resource/**", "/login.xhtml", "/ws/**", "/api/**").permitAll()
                .anyRequest().authenticated()//
                .and()
                .formLogin()//
                .loginPage("/login.xhtml").loginProcessingUrl("/j_spring_security_check")//
                .usernameParameter("j_username").passwordParameter("j_password")//
                .failureUrl("/login.xhtml?result=false").failureHandler(authFailureHandler).defaultSuccessUrl("/index.xhtml")
                .successHandler(authSuccessHandler).and().exceptionHandling().accessDeniedPage("/login")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login.xhtml")
                .deleteCookies("JSESSIONID").deleteCookies("remember-me").invalidateHttpSession(true) //
                .and().rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(1209600)//
                .and().httpBasic();


    }


/*    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userService);
        filter.setSuccessHandler(authSuccessHandler);
        filter.setFailureHandler(authFailureHandler);
        return filter;
    }*/


}