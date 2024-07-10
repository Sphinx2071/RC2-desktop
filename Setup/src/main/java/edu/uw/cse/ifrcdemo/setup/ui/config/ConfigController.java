package edu.uw.cse.ifrcdemo.setup.ui.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import org.apache.logging.log4j.Logger;

@Controller
public class ConfigController {
    private static final String SERVER_CONFIG = "config/config";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public ConfigController(Logger logger,  TemplateEngine templateEngine){
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @RequestMapping("config")
    public ModelAndView ConfigView() {
        ModelAndView modelAndView = new ModelAndView(SERVER_CONFIG);
        return modelAndView;
    }
}
