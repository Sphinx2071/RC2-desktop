package edu.uw.cse.ifrcdemo.setup.ui.config;

import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
import edu.uw.cse.ifrcdemo.setup.ui.config.ReliefConfig;
import org.apache.logging.log4j.Logger;
import org.apache.wink.json4j.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/reliefconfig")
@SessionAttributes(types = { ReliefConfig.class })
public class ConfigController {
    private static final String SERVER_LOGIN = "login/serverlogin";
    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";

    private static final String RELIEF_CONFIG = "reliefconfig/reliefConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public ConfigController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @Valid
    @ModelAttribute("ReliefConfig")
    public ReliefConfig newReliefConfig() {
        return new ReliefConfig();
    }

    @GetMapping("")
    public ModelAndView reliefServerConfig(
            @Valid @ModelAttribute("ReliefConfig") ReliefConfig reliefConfig,
            BindingResult bindingResult, SessionStatus status) throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {

        ModelAndView reliefModelAndView = new ModelAndView(RELIEF_CONFIG);

        reliefConfig.setAuthorizationTypeList(Arrays.asList(AuthorizationType.values()));
        reliefConfig.setRegistrationModeList(Arrays.asList(RegistrationMode.values()))   ;

        reliefModelAndView.addObject("reliefConfig", reliefConfig);
        return reliefModelAndView;
    }
}
