package edu.uw.cse.ifrcdemo.setupfun.ui.healthconfig;

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
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

@Controller
@RequestMapping("/healthconfig")
@SessionAttributes(types = { HealthConfigModel.class })
public class HealthConfigController {
    private static final String SERVER_LOGIN = "login/serverlogin";
    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";

    private static final String HEALTH_CONFIG = "healthConfig/healthConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public HealthConfigController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }
    @Valid @ModelAttribute("HealthConfigModel")
    public HealthConfigModel newHealthConfigModel() {
        return new HealthConfigModel();
    }

    @GetMapping("")
    public ModelAndView login(
            @Valid @ModelAttribute("HealthConfigModel") HealthConfigModel reliefConfigModel,
            BindingResult bindingResult, SessionStatus status) throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {
        return new ModelAndView(HEALTH_CONFIG);
    }
}
