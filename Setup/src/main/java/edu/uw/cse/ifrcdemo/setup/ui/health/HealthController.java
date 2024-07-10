package edu.uw.cse.ifrcdemo.setup.ui.health;

import edu.uw.cse.ifrcdemo.setup.model.healthConfig.HealthConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
import org.apache.logging.log4j.Logger;
import org.apache.wink.json4j.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import static edu.uw.cse.ifrcdemo.setup.model.healthConfig.HealthConfig.createConfig;

@Controller
@RequestMapping("/healthconfig")
@SessionAttributes(types = {HealthConfig.class})
public class HealthController {
    private static final String HEALTH_CONFIG = "healthconfig/healthConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    private HealthController(Logger logger, TemplateEngine templateEngine){
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @Valid
    @ModelAttribute("HealthConfig")
    public HealthConfig newHealthConfig(){
        return createConfig();
    }

    @GetMapping
    public ModelAndView healthServerConfig(
            @Valid @ModelAttribute("HealthConfig") HealthConfig healthConfig, BindingResult bindingResult, SessionStatus sessionStatus)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException{

        ModelAndView healthModelAndView = new ModelAndView(HEALTH_CONFIG);

        healthConfig.setRegistrationModeList(Arrays.asList((RegistrationMode.values())));

        healthModelAndView.addObject("healthConfig", healthConfig);
        return healthModelAndView;
    }
}
