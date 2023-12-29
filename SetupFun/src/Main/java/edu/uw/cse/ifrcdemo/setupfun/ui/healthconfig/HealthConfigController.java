package edu.uw.cse.ifrcdemo.setupfun.ui.healthconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HealthConfigController {

    private static final String HEALTH_CONFIG_TEMPLATE = "healthconfig/healthConfig";
    private static final Logger logger = LogManager.getLogger(HealthConfigController.class);



    public HealthConfigController() {
    }


    @RequestMapping("/healthconfig")
    public ModelAndView MainMenuView() {
        ModelAndView modelAndView = new ModelAndView(HEALTH_CONFIG_TEMPLATE);
        modelAndView.addObject("disableButtons", true);

        return modelAndView;
    }


}