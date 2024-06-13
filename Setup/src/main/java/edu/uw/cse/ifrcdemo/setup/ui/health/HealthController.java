package edu.uw.cse.ifrcdemo.setup.ui.health;

import edu.uw.cse.ifrcdemo.setup.model.health.HealthConfig;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
import org.apache.logging.log4j.Logger;
import org.apache.wink.json4j.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

@Controller
@RequestMapping("/healthconfig")
@SessionAttributes(types = { HealthConfig.class})
public class HealthController {
    private static final String HEALTH_CONFIG = "healthconfig/healthConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public HealthController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }


    @Valid
    @ModelAttribute("HealthConfig")
    public HealthConfig newHealthConfig() {
        return new HealthConfig();
    }

    @GetMapping("healthConfig")
    public ModelAndView healthServerConfig(
            @Valid @ModelAttribute("HealthConfig") HealthConfig healthConfig, BindingResult bindingResult, SessionStatus status)
            throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {

        ModelAndView healthModelAndView = new ModelAndView(HEALTH_CONFIG);

        healthConfig.setRegistrationModeList(Arrays.asList(RegistrationMode.values()))   ;

        healthModelAndView.addObject("healthConfig", healthConfig);
        return healthModelAndView;
    }
}