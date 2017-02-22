package com.spring.jsf.mixed.config;

import com.sun.faces.config.ConfigureListener;
import com.sun.faces.config.FacesInitializer;
import org.apache.log4j.Logger;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;


@Configuration
public class Application implements ServletContextAware {

    private static final Logger logger = Logger.getLogger(Application.class);


    private static final String securityFilter = "springSecurityFilterChain";
    private static final String securityFilterMapping = "/*";
    private static final String dispatcherServletName = "dispatcherServlet";
    private static final String dispatcherServletMapping = "/spring/*";
    private static final String messageDispatcherServletName = "messageDispatcherServlet";
    private static final String messageDispatcherServletMapping = "/ws/*";


    @Bean
    public RequestContextListener getRequestContextListener() {
        return new RequestContextListener();
    }


    @Bean
    public ServletRegistrationBean messageDispatcherServlet() {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setTransformWsdlLocations(true);

        ServletRegistrationBean registration = new ServletRegistrationBean(messageDispatcherServlet);
        registration.addInitParameter("transformWsdlLocations", "true");
        registration.setLoadOnStartup(1);
        registration.addUrlMappings(messageDispatcherServletMapping);
        registration.setName(messageDispatcherServletName);
        return registration;
    }


    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        //dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings(dispatcherServletMapping);
        registration.setLoadOnStartup(1);
        registration.setName(dispatcherServletName);
        return registration;
    }


   /* @Bean
    public FacesServlet facesServlet() {
        FacesServlet facesServlet = new FacesServlet();
        return facesServlet;
    }

    @Bean
    public ServletRegistrationBean facesServletRegistration(FacesServlet facesServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(facesServlet);
        registration.setLoadOnStartup(1);
        registration.setName("FacesServlet");
        registration.addUrlMappings("/faces*//*", "*.xhtml");
        return registration;
    }*/

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        ConfigureListener configureListener = new ConfigureListener();
        return new ServletListenerRegistrationBean<ConfigureListener>(
                configureListener);
    }

    @Bean
    public FilterRegistrationBean springSecurityFilterChain() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy(securityFilter);
        filterRegBean.setFilter(delegatingFilterProxy);
        filterRegBean.setName(securityFilter);
        filterRegBean.addUrlPatterns(securityFilterMapping);


        return filterRegBean;
    }


    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext", "resources.application");
        servletContext.setInitParameter("log4jConfiguration", "classpath:log4j.properties");
        servletContext.setInitParameter("log4jExposeWebAppRoot", "false");
        servletContext.setInitParameter("primefaces.THEME", "bootstrap-custom");
        servletContext.setInitParameter("facelets.LIBRARIES", "/WEB-INF/local.taglib.xml;/WEB-INF/springsecurity.taglib.xml");

        servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "client"); //client
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
        servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");


        /*Set<Class<?>> annotatedClasses = new HashSet<>();
        servletContext.setAttribute("com.sun.faces.AnnotatedClasses", annotatedClasses);*/
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
    }

    /*  @Bean
      public MimeMapper mimeMapper() {
          return new MimeMapper();
      }
  */
    public class MimeMapper implements EmbeddedServletContainerCustomizer {
        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
            mappings.add("ico", "image/x-icon");
            container.setMimeMappings(mappings);
        }
    }

    @Configuration
    public class JSFSetupBean implements ServletContextInitializer {

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            runServletInitializer(servletContext, new FacesInitializer(), "com.sun.faces", "javax.faces", "com.spring.jsf.mixed.ui.jsf", "org.primefaces");
        }


        private void runServletInitializer(ServletContext servletContext, FacesInitializer facesInitializer, String... basePackagesToScan)
                throws ServletException {
            ClassPathScanningCandidateComponentProvider scanner = constructScannerForServletInitializer(facesInitializer.getClass());
            Set<Class<?>> annotatedClasses = findAnnotatedClasses(scanner, basePackagesToScan);
            facesInitializer.onStartup(annotatedClasses, servletContext);
        }

        private ClassPathScanningCandidateComponentProvider constructScannerForServletInitializer(Class<? extends ServletContainerInitializer> initializerClass) {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            for (Class<?> class1 : initializerClass.getAnnotation(HandlesTypes.class).value()) {
                if (class1.isAnnotation()) {
                    logger.debug("Filtering on annotation " + class1);
                    scanner.addIncludeFilter(new AnnotationTypeFilter((Class<? extends Annotation>) class1));
                } else {
                    logger.debug("Filtering on parent class or interface " + class1);
                    scanner.addIncludeFilter(new AssignableTypeFilter(class1));
                }
            }
            return scanner;
        }

        private Set<Class<?>> findAnnotatedClasses(ClassPathScanningCandidateComponentProvider scanner, String... basePackagesToScan) {
            Set<Class<?>> annotatedClasses = new HashSet<>();
            for (String basePackage : basePackagesToScan) {
                logger.debug("Scanning under " + basePackage);
                scanner.findCandidateComponents(basePackage).forEach(bd -> {
                    annotatedClasses.add(ClassUtils.resolveClassName(bd.getBeanClassName(), getClass().getClassLoader()));
                });
            }
            return annotatedClasses;
        }

    }
}