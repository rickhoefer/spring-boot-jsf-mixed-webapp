package com.spring.jsf.mixed.ui.spring;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class GreetingRestController {
    static final Logger logger = Logger.getLogger(GreetingRestController.class);

    @RequestMapping(value = "/greeting/rest")
    public String greeting() {
        return "greeting";
    }


}
