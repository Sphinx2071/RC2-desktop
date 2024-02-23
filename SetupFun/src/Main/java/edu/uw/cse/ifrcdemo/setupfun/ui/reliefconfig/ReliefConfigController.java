package edu.uw.cse.ifrcdemo.setupfun.ui.reliefconfig;

import edu.uw.cse.ifrcdemo.setupfun.ui.login.ui.login.LoginFormModel;
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
@RequestMapping("/reliefconfig")
@SessionAttributes(types = { ReliefConfigModel.class })
public class ReliefConfigController {
    private static final String SERVER_LOGIN = "login/serverlogin";
    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";

    private static final String RELIEF_CONFIG = "reliefConfig/reliefConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public ReliefConfigController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }
    @Valid @ModelAttribute("ReliefConfigModel")
    public ReliefConfigModel newReliefConfigModel() {
        return new ReliefConfigModel();
    }

    @GetMapping("")
    public ModelAndView login(
            @Valid @ModelAttribute("ReliefConfigModel") ReliefConfigModel reliefConfigModel,
            BindingResult bindingResult, SessionStatus status) throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {
        return new ModelAndView(RELIEF_CONFIG);
    }
}
