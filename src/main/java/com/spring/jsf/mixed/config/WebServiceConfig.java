package com.spring.jsf.mixed.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SpringPlainTextPasswordValidationCallbackHandler;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

/*@EnableWs
@Configuration*/
public class WebServiceConfig extends WsConfigurerAdapter {
    static final Logger logger = Logger.getLogger(WebServiceConfig.class);
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Bean(name = "messageValidationService")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema guriSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("MessageValidation");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://com.cr/message/validation");
        wsdl11Definition.setSchema(guriSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema guriSchema() {
        return new SimpleXsdSchema(new ClassPathResource("guri.xsd"));
    }

    /*   @Autowired
       @Lazy
       private UserDetailsService  userDetailsService;

       @Bean
       public SpringDigestPasswordValidationCallbackHandler callbackHandler() {
           SpringDigestPasswordValidationCallbackHandler callbackHandler = new SpringDigestPasswordValidationCallbackHandler();
           callbackHandler.setUserDetailsService(userDetailsService);
           return callbackHandler;
       }
   */
    @Bean
    public SpringPlainTextPasswordValidationCallbackHandler callbackHandler() {
        SpringPlainTextPasswordValidationCallbackHandler callbackHandler = new SpringPlainTextPasswordValidationCallbackHandler();
        callbackHandler.setAuthenticationManager(authenticationManager);
        return callbackHandler;
    }


    @Bean
    public XwsSecurityInterceptor securityInterceptor() {

        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
        securityInterceptor.setCallbackHandler(callbackHandler());
        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));


        return securityInterceptor;
    }


    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(securityInterceptor());
    }


}


//http://memorynotfound.com/spring-ws-username-password-authentication-wss4j/