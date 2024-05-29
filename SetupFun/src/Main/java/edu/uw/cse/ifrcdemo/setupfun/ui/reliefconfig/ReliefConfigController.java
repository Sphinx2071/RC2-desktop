package edu.uw.cse.ifrcdemo.setupfun.ui.reliefconfig;

import com.sun.org.apache.xpath.internal.operations.Mod;
import edu.uw.cse.ifrcdemo.setupfun.ui.reliefconfig.ReliefConfigModel;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationStatus;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.AuthorizationType;
import edu.uw.cse.ifrcdemo.sharedlib.model.datattype.RegistrationMode;
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
@SessionAttributes(types = { ReliefConfigModel.class })
public class ReliefConfigController {
    private static final String SERVER_LOGIN = "login/serverlogin";
    private static final String MAIN_MENU_TEMPLATE = "mainmenu/mainMenu";

    private static final String RELIEF_CONFIG = "reliefconfig/reliefConfig";

    private final Logger logger;
    private final TemplateEngine templateEngine;

    public ReliefConfigController(Logger logger, TemplateEngine templateEngine) {
        this.logger = logger;
        this.templateEngine = templateEngine;
    }

    @Valid
    @ModelAttribute("ReliefConfigModel")
    public ReliefConfigModel newReliefConfigModel() {
        return new ReliefConfigModel();
    }

    @GetMapping("")
    public ModelAndView reliefServerConfig(
            @Valid @ModelAttribute("ReliefConfigModel") ReliefConfigModel reliefConfigModel,
            BindingResult bindingResult, SessionStatus status) throws IOException, JSONException, BackingStoreException, InvalidPreferencesFormatException {

        ModelAndView reliefModelAndView = new ModelAndView(RELIEF_CONFIG);

        reliefConfigModel.setAuthorizationTypeList(Arrays.asList(AuthorizationType.values()));

        reliefModelAndView.addObject("reliefConfigModel", reliefConfigModel);
        return reliefModelAndView;
    }
}
