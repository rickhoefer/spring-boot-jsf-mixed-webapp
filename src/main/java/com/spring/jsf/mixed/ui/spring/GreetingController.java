package com.spring.jsf.mixed.ui.spring;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController {
    static final Logger logger = Logger.getLogger(GreetingController.class);

    @RequestMapping(value = "/greeting")
    public String greeting() {
        return "greeting";
    }


}
